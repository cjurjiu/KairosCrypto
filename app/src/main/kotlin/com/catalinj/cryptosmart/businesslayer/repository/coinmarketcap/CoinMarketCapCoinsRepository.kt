package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoinDetails
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerPriceData
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.BoundedCryptoCoinsRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * [CoinsRepository] implementation that uses CoinMarketCap.com as prime provider of data.
 *
 * This repository implementation uses an offline-first approach: it first stores the coins in the
 * database, and only provides data from the database, never directly from the network.
 *
 * @constructor Creates a new instance of this repository.
 * @param cryptoSmartDb database object used to store the coins fetched over the network.
 * @param coinMarketCapService service used to fetch the coins from CoinMarketCap.com
 *
 * Created by catalinj on 28.01.2018.
 */
class CoinMarketCapCoinsRepository(private val cryptoSmartDb: CryptoSmartDb,
                                   private val coinMarketCapService: CoinMarketCapService)
    : CoinsRepository {

    override val loadingStateObservable: Observable<RequestState>

    private val cryptoCoinsRelay = BehaviorRelay.create<List<CryptoCoin>>()
    private val loadingStateRelay = BehaviorRelay.create<RequestState>()
    //worry about leaks later? sounds like a great idea!
    //TODO properly dispose subscription whenever this repository will have to die
    private val disposables: CompositeDisposable = CompositeDisposable()

    init {

        val connectableLoadingObservable = getLoadingObservable()
        disposables.add(connectableLoadingObservable.connect())
        loadingStateObservable = connectableLoadingObservable

        val coinListDisposable = setupCoinListObservable()
        disposables.add(coinListDisposable)
    }

    private fun setupCoinListObservable(): Disposable {
        return cryptoSmartDb.getCryptoCoinDao().getAllRx()
                .map { it.map { it.toBusinessLayerCoin() } }
                .debounce(200L, TimeUnit.MILLISECONDS)
                .subscribe {
                    //onNext
                    Log.d("RxJ", "monitor onNext")
                    cryptoCoinsRelay.accept(it)
                }
    }

    override fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>) {
        val apiRequest = BoundedCryptoCoinsRequest(startIndex = startIndex,
                numberOfCoins = numberOfCoins,
                coinMarketCapService = coinMarketCapService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            Log.d("RxJ", "repo getFreshCoins response do next coins size:" + it.data.size)
            //network coins from the response
            val networkCoins: List<CoinMarketCapCryptoCoin> = it.data.map { coin -> coin.value }
            //db coins & insert
            val dbCoins: List<DbPartialCryptoCoin> = networkCoins.map { coin -> coin.toDataLayerCoin() }
            cryptoSmartDb.getPlainCryptoCoinDao().insert(dbCoins)
            //price data
            val coinsPriceData = networkCoins.flatMap { it.toDataLayerPriceData() }
            cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(coinsPriceData)
            Log.d("RxJ", "repo getFreshCoins response AFTER do next coins size:" + it.data.size)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    override fun getCoinListObservable(): Observable<List<CryptoCoin>> {
        return cryptoCoinsRelay
    }

    override fun getCoinDetailsObservable(coinSymbol: String): Observable<CryptoCoinDetails> {
        return cryptoSmartDb.getCryptoCoinDao()
                .getCoinRx(coinSymbol)
                .map { dbCoinDetails ->
                    dbCoinDetails.toBusinessLayerCoinDetails()
                }
                .toObservable()
    }

    override fun fetchCoinDetails(coinId: String, errorHandler: Consumer<Throwable>) {
        val apiRequest = CryptoCoinDetailsRequest(coinId = coinId,
                coinMarketCapService = coinMarketCapService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            try {
                val coin = it.data
                Log.d("RxJ", "repo fetchCoinDetails response do next coins size:" + coin.name)
                val dbCoin: DbPartialCryptoCoin = coin.toDataLayerCoin()
                cryptoSmartDb.getPlainCryptoCoinDao().insert(dbCoin)
                val dbPriceData = coin.toDataLayerPriceData()
                cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(dbPriceData)
                Log.d("RxJ", "repo fetchCoinDetails response AFTER do next coins size:" + coin.name)
            } catch (e: NoSuchElementException) {
                errorHandler.accept(IllegalStateException("Server returned an empty list.", e))
            }
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    private fun getLoadingObservable(): ConnectableObservable<RequestState> {
        return loadingStateRelay.map { requestState ->
            return@map when (requestState) {
                RequestState.Idle -> 0
                RequestState.Loading -> 1
                is RequestState.Completed<*>, is RequestState.Error -> -1
            }
        }
                .scan { t1, t2 -> t1 + t2 }
                .map { intResult ->
                    Log.d("RxJ", "repo loading state result: $intResult")
                    return@map if (intResult > 0) {
                        RequestState.Loading
                    } else {
                        RequestState.Idle
                    }
                }
                .distinctUntilChanged()
                .publish()
    }

}