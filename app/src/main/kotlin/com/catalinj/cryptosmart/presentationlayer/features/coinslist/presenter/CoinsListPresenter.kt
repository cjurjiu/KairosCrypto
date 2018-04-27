package com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.presentationlayer.common.view.controller.LoadingController
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinListResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.CoinMarketCapCoinsRepository
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
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

    private val repository: CoinsRepository = CoinMarketCapCoinsRepository(db, coinMarketCapService)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var active: Boolean = false
    private var availableCoins: List<CryptoCoin>? = null
    private var view: CoinsListContract.CoinsListView? = null
    //loading logic...
    private var waitForLoad: Boolean = false
    private var loadingState: RequestState = RequestState.Idle
    private var loadingController: LoadingController? = null
    //init with default value. this will later be changed by user actions
    private var activeCurrency: String = resourceDecoder.decodeChangeCoinDialogItems().first().value
    //init with default value. this will later be changed by user actions
    private var activeSortOrder: String = resourceDecoder.fetchSortOptionsDialogItems().first().value
    //init with default value. this will later be changed by user actions
    private var activeSnapshot: String = resourceDecoder.decodeSnapshotDialogItems().first().value

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
    override fun coinSelected(selectedCoin: CryptoCoin) {
        view?.openCoinDetailsScreen(selectedCoin)
    }

    override fun changeCurrencyPressed() {
        view?.openChangeCurrencyDialog(resourceDecoder.decodeChangeCoinDialogItems(
                markedActive = activeCurrency
        ))
    }

    override fun sortListButtonPressed() {
        view?.openSortListDialog(resourceDecoder.fetchSortOptionsDialogItems(
                markedActive = activeSortOrder
        ))
    }

    override fun selectSnapshotButtonPressed() {
        view?.openSelectSnapshotDialog(resourceDecoder.decodeSnapshotDialogItems(
                markedActive = activeSnapshot
        ))
    }

    override fun displayCurrencyChanged(newSelectedCurrency: SelectionItem) {
        activeCurrency = newSelectedCurrency.value
        Log.d("Cata", "displayCurrencyChanged: selectionItem:${newSelectedCurrency.name}")
    }

    override fun listSortingChanged(newSortingOrder: SelectionItem) {
        activeSortOrder = newSortingOrder.value
        Log.d("Cata", "listSortingChanged: selectionItem:${newSortingOrder.name}")
    }

    override fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem) {
        activeSnapshot = newSelectedSnapshot.value
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

    private fun updateDisplayedCoins(coins: List<CryptoCoin>) {
        Log.d("RxJ", "Update displayed coins: size: ${coins.count()}")
        availableCoins = coins
        view?.setListData(coins)
        waitForLoad = false
    }

    private companion object {
        private const val COIN_FETCH_BATCH_SIZE = 100
    }
}