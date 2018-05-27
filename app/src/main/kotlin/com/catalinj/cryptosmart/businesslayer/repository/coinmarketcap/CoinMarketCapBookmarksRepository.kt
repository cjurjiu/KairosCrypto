package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap

import android.annotation.SuppressLint
import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerCoin
import com.catalinj.cryptosmart.businesslayer.converter.toDataLayerPriceData
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CopyOnWriteArrayList
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
class CoinMarketCapBookmarksRepository(private val cryptoSmartDb: CryptoSmartDb,
                                       private val coinMarketCapService: CoinMarketCapService,
                                       private val userSettings: CryptoSmartUserSettings)
    : BookmarksRepository {

    private val loadingCoinsList = CopyOnWriteArrayList<String>()

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

    override fun getBookmarks(): List<CryptoCoin> {
        return cryptoSmartDb.getBookmarksDao().getBookmarkedCoins().map { it.toBusinessLayerCoin() }
    }

    override fun refreshBookmarks(currencyRepresentation: CurrencyRepresentation,
                                  errorHandler: Consumer<Throwable>) {

        val requestsObservable: Observable<CryptoCoinDetailsRequest> = Observable.defer {
            Observable.fromIterable(cryptoSmartDb.getBookmarksDao().getBookmarkedCoins())
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map { bookmarkedCoin ->
                    CryptoCoinDetailsRequest(coinMarketCapService = coinMarketCapService,
                            requiredCurrency = currencyRepresentation,
                            coinId = bookmarkedCoin.cryptoCoin.serverId)
                }

        requestsObservable.subscribe({ request ->
            Log.d("RxJ", "OnNext reqest: $request")

            //onNext
            request.response.subscribe { coinDetailsResponse ->
                val coinDetails = coinDetailsResponse.data
                val coinId = cryptoSmartDb.getPlainCryptoCoinDao().insert(coinDetails.toDataLayerCoin())
                val priceDetailsIds = cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(coinDetails.toDataLayerPriceData())
                Log.d("RxJ", "repo refreshBookmarks response. inserted coin w id: " +
                        "$coinId and price data with ids: $priceDetailsIds")
            }
            request.errors.subscribe(errorHandler)
            request.state.subscribe { loadingStateRelay.accept(it) }
            request.execute()
        }, {
            //onError
            Log.d("RxJ", "OnError ftw")
        }, {
            //onComplete
            Log.d("RxJ", "OnComplete ftw")
        })
    }

    override fun isBookmarkLoading(coinSymbol: String): Boolean {
        return loadingCoinsList.contains(coinSymbol)
    }

    @SuppressLint("CheckResult")
    override fun refreshBookmarksById(currencyRepresentation: CurrencyRepresentation,
                                      bookmarks: List<BookmarksCoin>,
//                                      progressListener: Consumer<CryptoCoin>,
                                      errorHandler: Consumer<Throwable>) {
        Observable.defer { Observable.fromIterable(bookmarks) }
                .subscribeOn(Schedulers.io())
                .map { coin ->
                    Pair(coin, CryptoCoinDetailsRequest(coinMarketCapService = coinMarketCapService,
                            requiredCurrency = currencyRepresentation,
                            coinId = coin.id))
                }
                .subscribe({ coinRequestPair ->
                    val coin = coinRequestPair.first
                    val request = coinRequestPair.second
                    Log.d("RxJ", "OnNext reqest: $request")
                    //setup
                    request.response.subscribe { coinDetailsResponse ->
                        //onNext
                        val coinDetails = coinDetailsResponse.data
                        //also remove the coin from the list of loading coins before inserting into
                        //DB, to prevent db change notifications before the list is updated
                        loadingCoinsList.remove(coin.symbol)
                        val coinId = cryptoSmartDb.getPlainCryptoCoinDao().insert(coinDetails.toDataLayerCoin())
                        val priceDetailsIds = cryptoSmartDb.getCoinMarketCapPriceDataDao().insert(coinDetails.toDataLayerPriceData())
                        Log.d("RxJ", "repo refreshBookmarks response. inserted coin w id: " +
                                "$coinId and price data with ids: $priceDetailsIds")
                    }
                    request.errors.subscribe(errorHandler)
                    request.state.subscribe {
                        loadingStateRelay.accept(it)
                        if (it is RequestState.Idle.Finished) {
                            //if the request finished, then remove the coin from the list of
                            //loading coins
                            loadingCoinsList.remove(coin.symbol)
                        }
                    }
                    //add to the list of loading coins
                    loadingCoinsList.add(coin.symbol)
                    //launch request
                    request.execute()
                }, {
                    //onError
                    Log.d("RxJ", "OnError ftw")
                }, {
                    //onComplete
                    Log.d("RxJ", "OnComplete ftw")
                })
    }

    override fun getBookmarksListObservable(): Observable<List<CryptoCoin>> {
        return cryptoSmartDb.getBookmarksDao()
                .getBookmarksPriceDataFlowable(currency = userSettings.getPrimaryCurrency().currency)
                .map { it.map { it.toBusinessLayerCoin() } }
                .debounce(200L, TimeUnit.MILLISECONDS)
                .toObservable()
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