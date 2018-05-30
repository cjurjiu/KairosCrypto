package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoinDetails
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerPriceData
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.database.models.DbBookmark
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.BoundedCryptoCoinsRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * [CoinsRepository] implementation that uses CoinMarketCap.com as prime provider of data.
 *
 * This repository implementation uses an offline-first approach: it first stores the coins in the
 * database, and only provides data from the database, never directly from the network.
 *
 * @constructor Creates a new instance of this repository.
 * @param cryptoSmartDb database object used to store the coins fetched over the network.
 * @param coinMarketCapApiService service used to fetch the coins from CoinMarketCap.com
 *
 * Created by catalinj on 28.01.2018.
 */
class CoinMarketCapCoinsRepository(private val cryptoSmartDb: CryptoSmartDb,
                                   private val coinMarketCapApiService: CoinMarketCapApiService,
                                   private val userSettings: CryptoSmartUserSettings)
    : CoinsRepository {

    override val loadingStateObservable: Observable<Repository.LoadingState>

    private val loadingStateRelay = BehaviorRelay.create<RequestState>()
    //worry about leaks later? sounds like a great idea!
    //TODO properly dispose subscription whenever this repository will have to die
    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        val connectableLoadingObservable = getLoadingObservable()
        disposables.add(connectableLoadingObservable.connect())
        loadingStateObservable = connectableLoadingObservable
    }

    override fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>) {
        val apiRequest = BoundedCryptoCoinsRequest(startIndex = startIndex,
                numberOfCoins = numberOfCoins,
                coinMarketCapApiService = coinMarketCapApiService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            Log.d("RxJ", "repo getFreshCoins response do next coins size:" + it.data.size)
            //network coins from the response
            val networkCoins: List<CoinMarketCapCryptoCoin> = it.data.map { coin -> coin.value }
            //db coins & insert
            val dbCoins: List<DbPartialCryptoCoin> = networkCoins.map { coin -> coin.toDataLayerCoin() }
            cryptoSmartDb.getPlainCryptoCoinDao().insert(dbCoins)
            //price data
            val coinsPriceData = networkCoins.flatMap { it.toDataLayerPriceData() }
            val insertedDataIds = cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(coinsPriceData)
            Log.d("RxJ", "repo getFreshCoins response AFTER do next coins size:" + it.data.size + "" +
                    "inserted ids:" + insertedDataIds)
            insertDummyBookmarks(networkCoins)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    private fun insertDummyBookmarks(networkCoins: List<CoinMarketCapCryptoCoin>) {
        if (!userSettings.hasDummyBookmarks()) {
            val random = Random()
            val randomIndexes: MutableList<Int> = mutableListOf()
            randomIndexes.apply {
                add(random.nextInt(100))
                add(random.nextInt(100))
                add(random.nextInt(100))
                add(random.nextInt(100))
                add(random.nextInt(100))
            }
            var addedBookmarks = 0
            val randomBookmarks = networkCoins.filterIndexed { index, _ ->
                val shouldBeAdded = randomIndexes.contains(index) && (addedBookmarks < 10)
                if (shouldBeAdded) {
                    addedBookmarks += 1
                }
                return@filterIndexed shouldBeAdded
            }.mapIndexed { index, coin ->
                DbBookmark(0, coin.symbol, index.toLong())
            }
            val insertedIds = cryptoSmartDb.getBookmarksDao().insert(randomBookmarks)
            userSettings.saveHasDummyBookmarks(true)
            Log.d("Cata", "Fake bookmarks inserted. Id's: $insertedIds")
        } else {
            Log.d("Cata", "User already has fake bookmarks inserted.")
        }
    }

    override fun getCoinListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>> {
        return cryptoSmartDb.getCryptoCoinDao().getCryptoCoinsFlowable(currency = currencyRepresentation.currency)
                .map { it.map { it.toBusinessLayerCoin() } }
                .debounce(200L, TimeUnit.MILLISECONDS)
                .toObservable()
    }

    override fun getCoinDetailsObservable(coinSymbol: String): Observable<CryptoCoinDetails> {
        return cryptoSmartDb.getCryptoCoinDao()
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
            Log.d("RxJ", "state observables have finished, call onComplete to composite tracker.")
            compositeStateTracker.onComplete()
        }.subscribe()

        compositeStateTracker.observeOn(Schedulers.io()).reduce<Boolean>(true) { currentResult, newRequestState ->
            //accumulate the states of finished requests. In the end emit true if everything
            //was successful, emit false otherwise.
            Log.d("RxJ", "repo fetchCoinDetails reduce currentResult: $currentResult, newRequestState class:${newRequestState::class}")
            return@reduce (currentResult and (newRequestState is RequestState.Idle.Finished.Success<*>))
        }.subscribe { success ->
            Log.d("RxJ", "received coin details response!!")

            if (success) {
                //all requests went through and everything was fetched successfully.
                requestsList.forEach { request ->
                    request.response
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe { coinResponse ->
                                val coin = coinResponse.data
                                Log.d("RxJ", "Write coin data to db. Available price data:" +
                                        "${coin.quotes.keys}")
                                val dbCoin: DbPartialCryptoCoin = coin.toDataLayerCoin()
                                cryptoSmartDb.getPlainCryptoCoinDao().insert(dbCoin)
                                val dbPriceData = coin.toDataLayerPriceData()
                                val ids = cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(dbPriceData)
                                Log.d("RxJ", "Wrote coin data(${coin.name}) to db. " +
                                        "Available price data:${dbPriceData.map { it.currency }}. ids:$ids")
                            }
                }
            } else {
                requestsList.firstOrNull { request ->
                    if (request.errors.isEmpty.toFuture().get()) {
                        //if error stream is not empty, notify subscribe the error handler to it.
                        request.errors.subscribe(errorHandler)
                        return@firstOrNull true
                    } else {
                        return@firstOrNull false
                    }
                }
            }
        }

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
                    Log.d("RxJ", "repo loading state result: $intResult")
                    return@map if (intResult > 0) {
                        Repository.LoadingState.Loading
                    } else {
                        Repository.LoadingState.Idle
                    }
                }
                .distinctUntilChanged()
                .publish()
    }

}