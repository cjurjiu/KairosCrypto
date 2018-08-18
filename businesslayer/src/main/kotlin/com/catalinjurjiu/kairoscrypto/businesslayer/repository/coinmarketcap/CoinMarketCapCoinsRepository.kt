package com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toBusinessLayerCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toBusinessLayerCoinDetails
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toDataLayerCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toDataLayerPriceData
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.network.RequestState
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.request.BoundedCryptoCoinsRequest
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * [CoinsRepository] implementation that uses CoinMarketCap.com as prime provider of data.
 *
 * This repository implementation uses an offline-first approach: it first stores the coins in the
 * database, and only provides data from the database, never directly from the network.
 *
 * @constructor Creates a new instance of this repository.
 * @param kairosCryptoDb database object used to store the coins fetched over the network.
 * @param coinMarketCapApiService service used to fetch the coins from CoinMarketCap.com
 *
 * Created by catalinj on 28.01.2018.
 */
class CoinMarketCapCoinsRepository(private val kairosCryptoDb: KairosCryptoDb,
                                   private val coinMarketCapApiService: CoinMarketCapApiService)
    : CoinsRepository {

    override val loadingStateObservable: Observable<Repository.LoadingState>

    /**
     * Aggregates the loading state of all the requests made by this repository and emits only
     * when the loading state of the repository changes.
     *
     * If one or more requests are in flight, this emits a [Repository.LoadingState.Loading] object.
     * If not requests are in flight, then it emits a [Repository.LoadingState.Idle] object. Used by
     * the [loadingStateObservable].
     */
    private val loadingStateRelay = BehaviorRelay.create<RequestState>()

    //Dispose is never called currently on this composite disposable, since it only currently stores
    //the disposable of the loading observable, which needs to be active as long as this repository
    //is alive.
    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        val connectableLoadingObservable = getLoadingObservable()
        disposables.add(connectableLoadingObservable.connect())
        loadingStateObservable = connectableLoadingObservable
    }

    override fun fetchCoins(startIndex: Int,
                            numberOfCoins: Int,
                            currencyRepresentation: CurrencyRepresentation,
                            errorHandler: Consumer<Throwable>) {

        val apiRequest = BoundedCryptoCoinsRequest(startIndex = startIndex,
                numberOfCoins = numberOfCoins,
                currencyRepresentation = currencyRepresentation,
                coinMarketCapApiService = coinMarketCapApiService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            //network coins from the response
            val networkCoins: List<CoinMarketCapCryptoCoin> = it.data.map { coin -> coin.value }
            //db coins & insert
            val dbCoins: List<DbPartialCryptoCoin> = networkCoins.map { coin -> coin.toDataLayerCoin() }
            kairosCryptoDb.getPlainCryptoCoinDao().insert(dbCoins)
            //price data
            val coinsPriceData = networkCoins.flatMap { it.toDataLayerPriceData() }
            val insertedDataIds = kairosCryptoDb.getCoinMarketCapPriceDataDao().insert(coinsPriceData)
            Log.d(TAG, "Repo getFreshCoins response AFTER do next coins size:" + it.data.size + "" +
                    "inserted ids:" + insertedDataIds)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    override fun getCoinListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>> {
        return kairosCryptoDb.getCryptoCoinDao().getCryptoCoinsFlowable(currency = currencyRepresentation.currency)
                .map { it.map { it.toBusinessLayerCoin() } }
                .debounce(200L, TimeUnit.MILLISECONDS)
                .toObservable()
    }

    override fun getCoinDetailsObservable(coinSymbol: String): Observable<CryptoCoinDetails> {
        return kairosCryptoDb.getCryptoCoinDao()
                .getSingleCoinFlowable(coinSymbol)
                .map { dbCoinDetails ->
                    dbCoinDetails.toBusinessLayerCoinDetails()
                }
                .toObservable()
    }

    override fun fetchCoinDetails(coinId: String,
                                  valueRepresentationsArray: Array<CurrencyRepresentation>,
                                  errorHandler: Consumer<Throwable>) {

        val requestsList = mutableListOf<CryptoCoinDetailsRequest>()
        val compositeStateTracker: PublishSubject<RequestState> = PublishSubject.create()
        val observablesToZip = mutableListOf<Observable<RequestState>>()
        //iterate over the desired value representations
        valueRepresentationsArray.forEach {
            //create a request for each one
            val apiRequest = CryptoCoinDetailsRequest(coinId = coinId,
                    requiredCurrency = it,
                    coinMarketCapApiService = coinMarketCapApiService)
            apiRequest.state.doOnNext {
                //onNext
                //send state updates to the loading state relay
                loadingStateRelay.accept(it)
                if (it is RequestState.Idle.Finished.Error || it is RequestState.Idle.Finished.Success<*>) {
                    //and also to the composite state tracker for this call
                    compositeStateTracker.onNext(it)
                }
            }.subscribe()
            observablesToZip.add(apiRequest.state)

            //add the created request to the list of requests.
            requestsList.add(apiRequest)
        }

        Observable.concat(observablesToZip).doOnComplete {
            compositeStateTracker.onComplete()
        }.subscribe()

        compositeStateTracker.observeOn(Schedulers.io()).reduce<Boolean>(true) { currentResult, newRequestState ->
            //accumulate the states of finished requests. In the end emit true if everything
            //was successful, emit false otherwise.
            return@reduce (currentResult and (newRequestState is RequestState.Idle.Finished.Success<*>))
        }.subscribe({ success ->
            //onNext

            if (success) {
                //all requests went through and everything was fetched successfully.
                requestsList.forEach { request ->
                    request.response
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe { coinResponse ->
                                val coin = coinResponse.data
                                val dbCoin: DbPartialCryptoCoin = coin.toDataLayerCoin()
                                kairosCryptoDb.getPlainCryptoCoinDao().insert(dbCoin)
                                val dbPriceData = coin.toDataLayerPriceData()
                                val ids = kairosCryptoDb.getCoinMarketCapPriceDataDao().insert(dbPriceData)
                                Log.d(TAG, "Wrote coin data(${coin.name}) to db. " +
                                        "Available price data:${dbPriceData.map { it.currency }}. ids:$ids")
                            }
                }
            } else {
                requestsList.firstOrNull { request ->
                    if (!request.errors.isEmpty.toFuture().get()) {
                        //if error stream is not empty, notify subscribe the error handler to it.
                        request.errors.subscribe(errorHandler)
                        return@firstOrNull true
                    } else {
                        return@firstOrNull false
                    }
                }
            }
        }, {
            errorHandler.accept(it)
        })

        //execute requests
        requestsList.forEach {
            it.execute()
        }
    }

    private fun getLoadingObservable(): ConnectableObservable<Repository.LoadingState> {
        return loadingStateRelay.map { requestState ->
            return@map when (requestState) {
                RequestState.Idle.NotStarted -> 0
                RequestState.InFlight -> 1
                is RequestState.Idle.Finished.Error,
                is RequestState.Idle.Finished.Success<*> -> -1
            }
        }
                .scan { t1, t2 -> t1 + t2 }
                .map { intResult ->
                    Log.d(TAG, "Coin repo loading state result: $intResult")
                    return@map if (intResult > 0) {
                        Repository.LoadingState.Loading
                    } else {
                        Repository.LoadingState.Idle
                    }
                }
                .distinctUntilChanged()
                .publish()
    }

    private companion object {
        const val TAG = "CMCCoinsRepository"
    }
}