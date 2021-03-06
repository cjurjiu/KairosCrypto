package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.presenter

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.businesslayer.model.PredefinedSnapshot
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.threading.Executors
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.controller.LoadingController
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.contract.CoinsListContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.max

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListPresenter(private val repository: CoinsRepository,
                         userSettings: KairosCryptoUserSettings) :
        CoinsListContract.CoinsListPresenter {

    //overwritten properties
    //init with default value. this will later be changed by user actions
    override var displayCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
        set(value) {
            field = value
            displayCurrencyChanged(newSelectedCurrency = value)
        }
    //init with default value. this will later be changed by user actions
    override var displaySnapshot: PredefinedSnapshot = PredefinedSnapshot.SNAPSHOT_1H
        set(value) {
            field = value
            selectedSnapshotChanged(newSelectedSnapshot = value)
        }
    override var navigator: Navigator? = null
    //end overwritten properties

    //user interaction & output
    private var view: CoinsListContract.CoinsListView? = null
    //the composite disposable which stores all active disposables
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    //data to be displayed & manipulated
    private var availableCoins: List<CryptoCoin>? = null
    private lateinit var coinListDisposable: Disposable
    //loading logic...
    private var waitForLoad: Boolean = false
    private var loadingState: Repository.LoadingState = Repository.LoadingState.Idle
    private var loadingController: LoadingController? = null

    //base presenter methods
    override fun startPresenting() {
        //the primary currency can change, so we need to reload it each time we start presenting
        val loadingStateDisposable: Disposable = repository.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newLoadingState -> updateLoadingState(newLoadingState) }

        val coinListDisposable: Disposable = subscribeToCoinListUpdates(currency = displayCurrency)
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
    //end base presenter methods

    //coin list presenter methods
    override fun coinSelected(selectedCoin: CryptoCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin = selectedCoin)
    }

    override fun userPullToRefresh() {
        refreshCoins()
    }

    override fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int) {
        //fetch more coin logic
        if (currentScrollPosition / maxScrollPosition.toFloat() > 0.75f && !waitForLoad) {
            waitForLoad = true
            fetchMoreCoins()
        }
    }
    //end coin list presenter methods

    private var latestFetchedPage: Int = -1

    private fun fetchInitialBatch() {
        val subscription = repository.fetchCoins(pageIndex = 0, currencyRepresentation = displayCurrency)
                .observeOn(Schedulers.from(Executors.mainThread()))
                .subscribe({ fetchedPage ->
                    //onSuccess
                    this.latestFetchedPage = fetchedPage
                }, {
                    //onError
                    view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::fetchInitialBatch)
                })
        compositeDisposable.add(subscription)
    }

    private fun fetchMoreCoins() {
        val subscription = repository.fetchCoins(pageIndex = latestFetchedPage + 1,
                currencyRepresentation = displayCurrency)
                .observeOn(Schedulers.from(Executors.mainThread()))
                .subscribe({ fetchedPage ->
                    //onSuccess
                    this.latestFetchedPage = fetchedPage
                }, {
                    //onError
                    view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::fetchMoreCoins)
                })
        compositeDisposable.add(subscription)
    }

    private fun refreshCoins() {
        waitForLoad = false
        val subscription = repository.fetchCoins(pageIndex = 0,
                numberOfPages = max(latestFetchedPage, 1),
                currencyRepresentation = displayCurrency)
                .observeOn(Schedulers.from(Executors.mainThread()))
                .subscribe({ fetchedPage ->
                    //onSuccess
                    this.latestFetchedPage = fetchedPage
                }, {
                    //onError
                    view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::refreshCoins)
                })
        compositeDisposable.add(subscription)
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
        availableCoins = coins
        view?.setListData(coins)
        waitForLoad = false
    }

    private fun subscribeToCoinListUpdates(currency: CurrencyRepresentation): Disposable {
        return repository.getCoinListObservable(currency)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateDisplayedCoins(it)
                }
    }

    private fun displayCurrencyChanged(newSelectedCurrency: CurrencyRepresentation) {
        //remove old subscription
        compositeDisposable.remove(coinListDisposable)
        coinListDisposable.dispose()
        //subscribe to updates for the new currency
        coinListDisposable = subscribeToCoinListUpdates(displayCurrency)
        compositeDisposable.add(coinListDisposable)
        //hide the list while the list is being fetched
        view?.setContentVisible(isVisible = false)
        //launch the request
        fetchInitialBatch()
    }

    private fun selectedSnapshotChanged(newSelectedSnapshot: PredefinedSnapshot) {
        view?.refreshContent()
    }
}