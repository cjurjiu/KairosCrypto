package com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap

import android.annotation.SuppressLint
import android.util.Log
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toBusinessLayerCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toDataLayerCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toDataLayerPriceData
import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.network.RequestState
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.request.CryptoCoinDetailsRequest
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
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
 * @param kairosCryptoDb database object used to store the coins fetched over the network.
 * @param coinMarketCapApiService service used to fetch the coins from CoinMarketCap.com
 *
 * Created by catalinj on 28.01.2018.
 */
class CoinMarketCapBookmarksRepository(private val kairosCryptoDb: KairosCryptoDb,
                                       private val coinMarketCapApiService: CoinMarketCapApiService)
    : BookmarksRepository {

    override val loadingStateObservable: Observable<Repository.LoadingState>

    /**
     * List which stores the "coin symbol" of coins which are currently loading.
     */
    private val loadingCoinsList = CopyOnWriteArrayList<String>()

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

    override fun getBookmarks(): List<CryptoCoin> {
        return kairosCryptoDb.getBookmarksDao().getBookmarkedCoins().map { it.toBusinessLayerCoin() }
    }

    override fun refreshBookmarks(currencyRepresentation: CurrencyRepresentation,
                                  errorHandler: Consumer<Throwable>) {

        //create an observable which emits a CryptoCoinDetailsRequest for each coin returned by
        //BookmarksDao#getBookmarkedCoins()
        val requestsObservable: Observable<CryptoCoinDetailsRequest> = Observable.defer {
            Observable.fromIterable(kairosCryptoDb.getBookmarksDao().getBookmarkedCoins())
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map { bookmarkedCoin ->
                    CryptoCoinDetailsRequest(coinMarketCapApiService = coinMarketCapApiService,
                            requiredCurrency = currencyRepresentation,
                            coinId = bookmarkedCoin.cryptoCoin.serverId)
                }

        //subscribe to each CryptoCoinDetailsRequest's response, error & loading streams, then
        //execute start the request.
        requestsObservable.subscribe({ request ->
            //onNext
            request.response.subscribe { coinDetailsResponse ->
                val coinDetails = coinDetailsResponse.data.entries.first().value
                val coinId = kairosCryptoDb.getPlainCryptoCoinDao().insert(coinDetails.toDataLayerCoin())
                val priceDetailsIds = kairosCryptoDb.getCoinMarketCapPriceDataDao().insert(coinDetails.toDataLayerPriceData())
                Log.d(TAG, "Repo refreshBookmarks response. inserted coin with id: " +
                        "$coinId and price data with ids: $priceDetailsIds")
            }
            request.errors.subscribe(errorHandler)
            request.state.subscribe { loadingStateRelay.accept(it) }
            request.execute()
        }, {
            //onError
            Log.d(TAG, "Bookmarks refresh onError: $it.")
            errorHandler.accept(it)
        }, {
            //onComplete
            Log.d(TAG, "Bookmarks refresh finished successfully.")
        })
    }

    override fun isBookmarkLoading(coinSymbol: String): Boolean {
        return loadingCoinsList.contains(coinSymbol)
    }

    @SuppressLint("CheckResult")
    override fun refreshBookmarksById(currencyRepresentation: CurrencyRepresentation,
                                      bookmarks: List<BookmarksCoin>,
                                      errorHandler: Consumer<Throwable>) {
        Observable.defer { Observable.fromIterable(bookmarks) }
                .subscribeOn(Schedulers.io())
                .map { coin ->
                    Pair(coin, CryptoCoinDetailsRequest(coinMarketCapApiService = coinMarketCapApiService,
                            requiredCurrency = currencyRepresentation,
                            coinId = coin.id))
                }
                .subscribe({ coinRequestPair ->
                    val coin = coinRequestPair.first
                    val request = coinRequestPair.second
                    //setup
                    request.response.subscribe { coinDetailsResponse ->
                        //onNext
                        val coinDetails = coinDetailsResponse.data.entries.first().value
                        //also remove the coin from the list of loading coins before inserting into
                        //DB, to prevent db change notifications before the list is updated
                        loadingCoinsList.remove(coin.symbol)
                        val coinId = kairosCryptoDb.getPlainCryptoCoinDao().insert(coinDetails.toDataLayerCoin())
                        val priceDetailsIds = kairosCryptoDb.getCoinMarketCapPriceDataDao().insert(coinDetails.toDataLayerPriceData())
                        Log.d(TAG, "Repo refreshBookmarks response. inserted coin with id: " +
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
                    Log.d(TAG, "Bookmarks refresh by id onError: $it.")
                    errorHandler.accept(it)
                }, {
                    //onComplete
                    Log.d(TAG, "Bookmarks refresh by id finished successfully.")
                })
    }

    override fun getBookmarksListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>> {
        return kairosCryptoDb.getBookmarksDao()
                .getBookmarksPriceDataFlowable(currency = currencyRepresentation.currency)
                .map { it.map { it.toBusinessLayerCoin() } }
                .debounce(200L, TimeUnit.MILLISECONDS)
                .toObservable()
    }

    override fun isBookmark(coinSymbol: String): Single<Boolean> =
            kairosCryptoDb.getBookmarksDao()
                    .getBookmarks()
                    .onErrorReturn { emptyList() }
                    .flattenAsObservable { list -> list }
                    .filter {
                        it.bookmarkedCoinSymbol == coinSymbol
                    }
                    .isEmpty
                    .map { !it }

    override fun addBookmark(coinSymbol: String): Single<Boolean> {
        return Single.fromCallable {
            kairosCryptoDb.getBookmarksDao()
                    .insert(DbBookmark(0, coinSymbol, System.currentTimeMillis()))
        }
                .onErrorReturn { 0 }
                .map { it > 0 }
    }

    override fun deleteBookmark(coinSymbol: String): Single<Boolean> {
        return kairosCryptoDb.getBookmarksDao().getBookmark(coinSymbol)
                .flatMap {
                    Single.just(kairosCryptoDb.getBookmarksDao().delete(it))
                }
                .onErrorReturnItem(-1)
                .map {
                    //true if something was deleted
                    it > 0
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
                    Log.d(TAG, "Bookmarks repo loading state result: $intResult")
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
        const val TAG = "CMCBookmarksRepository"
    }
}