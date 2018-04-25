package com.catalinj.cryptosmart.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.common.controller.LoadingController
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.features.coinslist.view.CoinListResourceDecoder
import com.catalinj.cryptosmart.features.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.network.RequestState
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.repository.CoinsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListPresenter(private val resourceDecoder: CoinListResourceDecoder,
                         db: CryptoSmartDb,
                         coinMarketCapService: CoinMarketCapService) :
        CoinsListContract.CoinsListPresenter {

    private val repository = CoinsRepository(db, coinMarketCapService)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var active: Boolean = false
    private var availableCoins: List<CoinMarketCapCryptoCoin>? = null
    private var view: CoinsListContract.CoinsListView? = null
    //loading logic...
    private var waitForLoad: Boolean = false
    private var loadingState: RequestState = RequestState.Idle
    private var loadingController: LoadingController? = null

    init {
        Log.d("Cata", "Injected CoinListPresenter")
    }

    //base presenter methods
    override fun startPresenting() {
        if (active) {
            //do nothing, already presenting
        }
        active = true
        val loadingStateDisposable: Disposable = repository.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newLoadingState -> updateLoadingState(newLoadingState) })

        val cryptoObservable: Disposable = repository.cryptoCoinObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("RxJ", "Update coins")
                    updateDisplayedCoins(it)
                })

        compositeDisposable.add(loadingStateDisposable)
        compositeDisposable.add(cryptoObservable)
    }

    override fun stopPresenting() {
        active = false
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: CoinsListContract.CoinsListView) {
        this.view = view
        view.initialise()
        this.loadingController = LoadingController(view)
    }

    override fun viewInitialised() {
        setLoadingState(loadingState)
        availableCoins.orEmpty().apply {
            if (size > 0) {
                view?.setListData(this)
            } else {
                fetchMoreCoins()
            }
        }
    }

    override fun viewDestroyed() {
        this.loadingController?.cleanUp()
        this.view = null
        this.loadingController = null
    }

    override fun getView(): CoinsListContract.CoinsListView? {
        return this.view
    }

    //coin list presenter methods
    override fun coinSelected(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeCurrencyPressed() {
        view?.openChangeCurrencyDialog(resourceDecoder.decodeChangeCoinListItems())
    }

    override fun sortListButtonPressed() {
        view?.openSortListDialog(resourceDecoder.decodeSortListItems())
    }

    override fun selectSnapshotButtonPressed() {
        view?.openSelectSnapshotDialog(resourceDecoder.decodeSnapShotListItems())
    }

    override fun displayCurrencyChanged(newSelectedCurrency: SelectionItem) {
        Log.d("Cata", "displayCurrencyChanged: selectionItem:${newSelectedCurrency.name}")
    }

    override fun listSortingChanged(newSortingOrder: SelectionItem) {
        Log.d("Cata", "listSortingChanged: selectionItem:${newSortingOrder.name}")
    }

    override fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem) {
        Log.d("Cata", "selectedSnapshotChanged: selectionItem:${newSelectedSnapshot.name}")
    }

    override fun userPullToRefresh() {
        Log.d("Cata", "PULL TO REFRESH")
        refreshCoins()
    }

    override fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int) {
        Log.d("Cata2", "Scroll! position:$currentScrollPosition maxScroll:$maxScrollPosition")
        if (currentScrollPosition / maxScrollPosition.toFloat() > 0.75f && !waitForLoad) {
            waitForLoad = true
            fetchMoreCoins()
        }
    }

    private fun fetchMoreCoins() {
        Log.d("Cata", "CoinListPresenter#fetchMoreCoins")
        val startIndex = availableCoins.orEmpty().size
        repository.fetchCoins(startIndex = startIndex, numberOfCoins = COIN_FETCH_BATCH_SIZE,
                errorHandler = Consumer { Log.d("RxJ", "Fetch more coins error:+ $it") })
    }

    private fun refreshCoins() {
        Log.d("Cata", "CoinListPresenter#refreshCoins")
        waitForLoad = false
        val fetchedCoinsNumber = availableCoins?.count() ?: COIN_FETCH_BATCH_SIZE
        repository.fetchCoins(startIndex = 0, numberOfCoins = fetchedCoinsNumber,
                errorHandler = Consumer { Log.d("RxJ", "Refresh coins error: $it") })
    }

    private fun updateLoadingState(loadingState: RequestState) {
        this.loadingState = loadingState
        setLoadingState(loadingState)
    }

    private fun setLoadingState(loadingState: RequestState) {
        when (loadingState) {
            RequestState.Idle -> loadingController?.hide()
            RequestState.Loading -> loadingController?.show()
            else -> loadingController?.hide()
        }
    }

    private fun updateDisplayedCoins(coins: List<CoinMarketCapCryptoCoin>) {
        Log.d("RxJ", "Update displayed coins: size: ${coins.count()}")
        availableCoins = coins
        view?.setListData(coins)
        waitForLoad = false
    }

    private companion object {
        private const val COIN_FETCH_BATCH_SIZE = 100
    }
}