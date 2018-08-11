package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.presenter

import android.util.Log
import com.catalinjurjiu.kairoscrypto.businesslayer.model.*
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.threading.Executors
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.controller.LoadingController
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.contract.BookmarksContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksPresenter(private val bookmarksRepository: BookmarksRepository,
                         userSettings: KairosCryptoUserSettings) : BookmarksContract.BookmarksPresenter {

    //overwritten properties

    //init with default value. this will later be changed by user actions
    override var displayCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
        set(value) {
            field = value
            displayCurrencyChanged(newDisplayCurrency = value)
        }
    //init with default value. this will later be changed by user actions
    override var displaySnapshot: PredefinedSnapshot = PredefinedSnapshot.SNAPSHOT_1H
        set(value) {
            field = value
            selectedSnapshotChanged(newSnapshot = value)
        }
    override var navigator: Navigator? = null
    //end overwritten properties

    private var loadController: LoadingController? = null
    private var bookmarksView: BookmarksContract.BookmarksView? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var bookmarksListDisposable: Disposable
    private var availableData: List<BookmarksCoin>? = null

    //base presenter methods
    override fun startPresenting() {
        val loadingDisposable = bookmarksRepository.loadingStateObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is Repository.LoadingState.Loading -> loadController?.show()
                        is Repository.LoadingState.Idle -> loadController?.hide()
                    }
                }
        bookmarksListDisposable = subscribeToBookmarksListUpdates(currency = displayCurrency)

        compositeDisposable.add(loadingDisposable)
        compositeDisposable.add(bookmarksListDisposable)

        val data = availableData
        if (data == null) {
            refreshBookmarks()
        } else {
            setViewData(data)
        }

        Log.d("Cata", "$TAG#startPresenting")
    }

    override fun stopPresenting() {
        Log.d("Cata", "$TAG#stopPresenting")
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: BookmarksContract.BookmarksView) {
        Log.d("Cata", "$TAG#viewAvailable")
        loadController = LoadingController(view)
        bookmarksView = view
        view.initialise()
    }

    override fun viewDestroyed() {
        Log.d("Cata", "$TAG#viewDestroyed")
        navigator = null
        loadController?.cleanUp()
        loadController = null
    }

    override fun getView(): BookmarksContract.BookmarksView? {
        Log.d("Cata", "$TAG#getView")
        return bookmarksView
    }
    //END base presenter methods

    //bookmarks presenter methods
    override fun coinSelected(cryptoCoin: BookmarksCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin.toBusinessLayerCoin())
    }

    override fun userPullToRefresh() {
        refreshBookmarks()
        bookmarksView?.hideLoadingIndicator()
    }
    //end bookmarks presenter logic

    private fun setViewData(data: List<BookmarksCoin>) {
        bookmarksView?.setListData(data)
    }

    private fun refreshBookmarks() {
        val disposable = Single.fromCallable { bookmarksRepository.getBookmarks() }
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.map { it.toBookmarksCoin(isLoading = true) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    bookmarksView?.setListData(it)
                }
                .observeOn(Schedulers.io())
                .subscribe { bookmarks ->
                    bookmarksRepository.refreshBookmarksById(currencyRepresentation = displayCurrency,
                            bookmarks = bookmarks,
                            errorHandler = Consumer {
                                Log.d("Cata", "Error occurred at refreshBookmarks:$it")
                                Executors.mainThread().execute {
                                    bookmarksView?.showError(errorCode = ErrorCode.GENERIC_ERROR,
                                            retryAction = ::refreshBookmarks)
                                }
                            })
                }
        compositeDisposable.add(disposable)
    }

    private fun subscribeToBookmarksListUpdates(currency: CurrencyRepresentation): Disposable {
        return bookmarksRepository.getBookmarksListObservable(currencyRepresentation = currency)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { coinList ->
                    //ensure content is visible
                    bookmarksView?.setContentVisible(isVisible = true)
                    Log.d("Cata", "Got new bookmarks! activeCurrency: $currency")
                    setViewData(coinList.map { coin ->
                        coin.toBookmarksCoin(bookmarksRepository.isBookmarkLoading(coinSymbol = coin.symbol))
                    })
                }
    }

    private fun displayCurrencyChanged(newDisplayCurrency: CurrencyRepresentation) {
        Log.d("Cata", "$TAG#displayCurrencyChanged")
        //remove old subscription
        compositeDisposable.remove(bookmarksListDisposable)
        bookmarksListDisposable.dispose()
        //subscribe to updates for the new currency
        bookmarksListDisposable = subscribeToBookmarksListUpdates(currency = newDisplayCurrency)
        compositeDisposable.add(bookmarksListDisposable)
        //hide the list while the list is being fetched
        bookmarksView?.setContentVisible(isVisible = false)
        //launch the request
        bookmarksRepository.refreshBookmarks(currencyRepresentation = newDisplayCurrency,
                errorHandler = Consumer {
                    Log.d("Cata", "Error occurred at displayCurrencyChanged:$it")
                })
    }

    private fun selectedSnapshotChanged(newSnapshot: PredefinedSnapshot) {
        Log.d("Cata", "selectedSnapshotChanged: selectionItem:${newSnapshot.name}")
        bookmarksView?.refreshContent()
    }

    companion object {
        const val TAG = "BookmarksPresenter"
    }
}