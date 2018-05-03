package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerCoinDetails
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoinDetails
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.BoundedCryptoCoinsRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

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

    override val cryptoCoinObservable: Observable<List<CryptoCoin>>
        get() {
            return cryptoCoinsRelay
        }
    override val loadingStateObservable: Observable<RequestState>

    private val cryptoCoinsRelay = BehaviorRelay.create<List<CryptoCoin>>()
    private val loadingStateRelay = BehaviorRelay.create<RequestState>()

    init {
        loadingStateObservable = getLoadingObservable()
        cryptoSmartDb.getCoinMarketCapCryptoCoinDao().monitorRx().subscribe({
            Log.d("RxJ", "monitor onNext")
            cryptoCoinsRelay.accept(it.map { coin -> coin.toBusinessLayerCoin() })
        })
    }

    override fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>) {
        val apiRequest = BoundedCryptoCoinsRequest(startIndex = startIndex,
                numberOfCoins = numberOfCoins,
                coinMarketCapService = coinMarketCapService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            Log.d("RxJ", "repo getFreshCoins response do next coins size:" + it.size)
            val dbCoins: List<DbCryptoCoin> = it.map { coin -> coin.toDataLayerCoin() }
            cryptoSmartDb.getCoinMarketCapCryptoCoinDao().insert(dbCoins)
            Log.d("RxJ", "repo getFreshCoins response AFTER do next coins size:" + it.size)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    override fun fetchCoinDetails(coinId: String, errorHandler: Consumer<Throwable>) {
        val apiRequest = CryptoCoinDetailsRequest(coinId = coinId,
                coinMarketCapService = coinMarketCapService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            Log.d("RxJ", "repo fetchCoinDetails response do next coins size:" + it.name)
            val dbCoin: DbCryptoCoinDetails = it.toDataLayerCoinDetails()
            cryptoSmartDb.getCoinMarketCapCryptoCoinDetailsDao().insert(dbCoin)
            Log.d("RxJ", "repo fetchCoinDetails response AFTER do next coins size:" + it.name)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    private fun getLoadingObservable(): Observable<RequestState> {
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
                }.distinctUntilChanged()
    }

}