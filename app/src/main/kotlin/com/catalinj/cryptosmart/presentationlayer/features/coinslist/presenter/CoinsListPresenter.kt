package com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.businesslayer.model.PredefinedSnapshot
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.common.decoder.SelectionItemsResource
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.threading.Executors
import com.catalinj.cryptosmart.presentationlayer.common.view.controller.LoadingController
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListPresenter(private val resourceDecoder: ResourceDecoder,
                         private val userSettings: CryptoSmartUserSettings,
                         private val repository: CoinsRepository) :
        CoinsListContract.CoinsListPresenter {

    //user interaction & output
    private var view: CoinsListContract.CoinsListView? = null
    override var navigator: Navigator? = null
    //the composite disposable which stores all active disposables
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    //data to be displayed & manipulated
    private var availableCoins: List<CryptoCoin>? = null
    private lateinit var coinListDisposable: Disposable
    //loading logic...
    private var waitForLoad: Boolean = false
    private var loadingState: Repository.LoadingState = Repository.LoadingState.Idle
    private var loadingController: LoadingController? = null
    //init with default value. this will later be changed by user actions
    private var activeCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
    private lateinit var changeCurrencyDialogItems: List<SelectionItem>
    //init with default value. this will later be changed by user actions
    private var activeSnapshot: PredefinedSnapshot = PredefinedSnapshot.SNAPSHOT_1H
    private lateinit var changeSnapshotDialogItems: List<SelectionItem>

    //base presenter methods
    override fun startPresenting() {
        //the primary currency can change, so we need to reload it each time we start presenting
        initChangeCurrencyDialogItems()
        ensureSnapshotDialogOptionsInitialised()
        val loadingStateDisposable: Disposable = repository.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newLoadingState -> updateLoadingState(newLoadingState) }

        val coinListDisposable: Disposable = subscribeToCoinListUpdates(currency = activeCurrency)
        this.coinListDisposable = coinListDisposable

        compositeDisposable.add(loadingStateDisposable)
        compositeDisposable.add(coinListDisposable)

        availableCoins.orEmpty().apply {
            if (isNotEmpty()) {
                view?.setListData(this)
            } else {
                fetchInitialBatch()
            }
        }
    }

    override fun stopPresenting() {
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: CoinsListContract.CoinsListView) {
        this.view = view
        view.initialise()
        this.loadingController = LoadingController(view)
    }

    override fun viewDestroyed() {
        this.loadingController?.cleanUp()
        this.view = null
        this.loadingController = null
        this.navigator = null
    }

    override fun getView(): CoinsListContract.CoinsListView? {
        return this.view
    }

    //coin list presenter methods
    override fun coinSelected(selectedCoin: CryptoCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin = selectedCoin)
    }

    override fun changeCurrencyButtonPressed() {
        view?.openChangeCurrencyDialog(selectionItems = changeCurrencyDialogItems)
    }

    override fun selectSnapshotButtonPressed() {
        view?.openSelectSnapshotDialog(changeSnapshotDialogItems)
    }

    override fun getSelectedCurrency(): CurrencyRepresentation = activeCurrency

    override fun getSelectedSnapshot(): PredefinedSnapshot = activeSnapshot

    override fun displayCurrencyChanged(newSelectedCurrency: SelectionItem) {
        if (activeCurrency.currency == newSelectedCurrency.value) {
            //the new selection is equal to the previous one. do nothing
            return
        }
        activeCurrency = CurrencyRepresentation.valueOf(newSelectedCurrency.value.toUpperCase())
        refreshActiveCurrencyForSelectionList(changeCurrencyDialogItems)
        //remove old subscription
        compositeDisposable.remove(coinListDisposable)
        coinListDisposable.dispose()
        //subscribe to updates for the new currency
        coinListDisposable = subscribeToCoinListUpdates(activeCurrency)
        compositeDisposable.add(coinListDisposable)
        //hide the list while the list is being fetched
        view?.setContentVisible(isVisible = false)
        //launch the request
        fetchInitialBatch()
        Log.d("Cata", "displayCurrencyChanged: selectionItem:${newSelectedCurrency.name}")
    }

    override fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem) {
        if (activeSnapshot.stringValue == newSelectedSnapshot.value) {
            //the new selection is equal to the previous one. do nothing
            return
        }
        activeSnapshot = PredefinedSnapshot.of(newSelectedSnapshot.value)
        refreshSelectedSnapshot(changeSnapshotDialogItems)
        view?.refreshContent()
        Log.d("Cata", "selectedSnapshotChanged: selectionItem:${newSelectedSnapshot.name}")
    }

    override fun userPullToRefresh() {
        Log.d("Cata", "PULL TO REFRESH")
        refreshCoins()
    }

    override fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int) {
        Log.d("Cata2", "Scroll! position:$currentScrollPosition maxScroll:$maxScrollPosition")

        //fetch more coin logic
        if (currentScrollPosition / maxScrollPosition.toFloat() > 0.75f && !waitForLoad) {
            Log.d("Cata2", "scroll trigger load!")
            waitForLoad = true
            fetchMoreCoins()
        }

        //scroll to top button hide/reveal logic
        val displayedItemPosition = (view?.getDisplayedItemPosition() ?: 0)
        val scrollToTopVisible = view?.isScrollToTopVisible() ?: false
        if (displayedItemPosition > SCROLL_TO_TOP_LIST_THRESHOLD && !scrollToTopVisible) {
            view?.revealScrollToTopButton()
        } else if (displayedItemPosition < SCROLL_TO_TOP_LIST_THRESHOLD && scrollToTopVisible) {
            view?.hideScrollToTopButton()
        }
    }

    override fun scrollToTopPressed() {
        view?.scrollTo(0)
        view?.hideScrollToTopButton()
    }

    private fun fetchInitialBatch() {
        Log.d("Cata", "CoinListPresenter#fetchMoreCoins")
        val startIndex = 0
        repository.fetchCoins(startIndex = startIndex,
                numberOfCoins = COIN_FETCH_BATCH_SIZE,
                currencyRepresentation = activeCurrency,
                errorHandler = Consumer {
                    Executors.mainThread().execute {
                        view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::fetchInitialBatch)
                    }
                })
    }

    private fun fetchMoreCoins() {
        Log.d("Cata", "CoinListPresenter#fetchMoreCoins")
        val startIndex = availableCoins.orEmpty().size
        repository.fetchCoins(startIndex = startIndex,
                numberOfCoins = COIN_FETCH_BATCH_SIZE,
                currencyRepresentation = activeCurrency,
                errorHandler = Consumer {
                    Executors.mainThread().execute {
                        view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::fetchMoreCoins)
                    }
                })
    }

    private fun refreshCoins() {
        Log.d("Cata", "CoinListPresenter#refreshCoins")
        waitForLoad = false
        val fetchedCoinsNumber = availableCoins?.count() ?: COIN_FETCH_BATCH_SIZE
        repository.fetchCoins(startIndex = 0,
                numberOfCoins = fetchedCoinsNumber,
                currencyRepresentation = activeCurrency,
                errorHandler = Consumer {
                    Log.d("Cata", "CoinListPresenter#refreshCoins error: $it")

                    Executors.mainThread().execute {
                        view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::refreshCoins)
                    }
                })
    }

    private fun updateLoadingState(loadingState: Repository.LoadingState) {
        this.loadingState = loadingState
        setLoadingState(loadingState)
    }

    private fun setLoadingState(loadingState: Repository.LoadingState) {
        when (loadingState) {
            Repository.LoadingState.Idle -> loadingController?.hide()
            Repository.LoadingState.Loading -> loadingController?.show()
        }
    }

    private fun updateDisplayedCoins(coins: List<CryptoCoin>) {
        //ensure the content is visible
        view?.setContentVisible(isVisible = true)
        Log.d("RxJ", "Update displayed coins: size: ${coins.count()}")
        availableCoins = coins
        view?.setListData(coins)
        waitForLoad = false
    }

    private fun subscribeToCoinListUpdates(currency: CurrencyRepresentation): Disposable {
        return repository.getCoinListObservable(currency)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("RxJ", "Update coins")
                    updateDisplayedCoins(it)
                }
    }

    private fun initChangeCurrencyDialogItems() {
        val selectionList = resourceDecoder
                .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.CURRENCIES)
                .toMutableList()
        val primaryCurrency = userSettings.getPrimaryCurrency()
        selectionList.add(index = 0,
                element = SelectionItem(name = primaryCurrency.name, value = primaryCurrency.currency)
        )
        refreshActiveCurrencyForSelectionList(selectionList)
        changeCurrencyDialogItems = selectionList
    }

    private fun refreshActiveCurrencyForSelectionList(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == activeCurrency.currency
        }
    }

    private fun refreshSelectedSnapshot(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == activeSnapshot.stringValue
        }
    }

    private fun ensureSnapshotDialogOptionsInitialised() {
        if (!::changeSnapshotDialogItems.isInitialized) {
            val snapshotOptions: List<SelectionItem> = resourceDecoder
                    .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.SNAPSHOTS)
            snapshotOptions.onEach {
                it.isActive = it.value == activeSnapshot.stringValue
            }
            changeSnapshotDialogItems = snapshotOptions
        }
    }

    private companion object {
        private const val COIN_FETCH_BATCH_SIZE = 100
        private const val SCROLL_TO_TOP_LIST_THRESHOLD = 30
    }
}