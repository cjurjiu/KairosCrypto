package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.common.decoder.SelectionItemsResource
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.view.controller.LoadingController
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.toBookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.toBusinessLayerCoin
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksPresenter(private val resourceDecoder: ResourceDecoder,
                         private val bookmarksRepository: BookmarksRepository,
                         private val userSettings: CryptoSmartUserSettings) : BookmarksContract.BookmarksPresenter {

    override var navigator: Navigator? = null
    private var loadController: LoadingController? = null
    private var bookmarksView: BookmarksContract.BookmarksView? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var bookmarksListDisposable: Disposable
    private var availableData: List<BookmarksCoin>? = null
    private var activeCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
    private lateinit var changeCurrencyDialogItems: List<SelectionItem>
    //init with default value. this will later be changed by user actions
    private var activeSnapshotValue: String = resourceDecoder
            .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.SNAPSHOTS)
            .first()
            .value
    private lateinit var changeSnapshotDialogItems: List<SelectionItem>

    //base presenter methods
    override fun startPresenting() {
        initChangeCurrencyDialogItems()
        ensureSnapshotDialogOptionsInitialised()
        val loadingDisposable = bookmarksRepository.loadingStateObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is Repository.LoadingState.Loading -> loadController?.show()
                        is Repository.LoadingState.Idle -> loadController?.hide()
                    }
                }
        bookmarksListDisposable = subscribeToBookmarksListUpdates(activeCurrency)

        compositeDisposable.add(loadingDisposable)
        compositeDisposable.add(bookmarksListDisposable)

        val data = availableData
        if (data == null) {
            refreshBookmarks()
        } else {
            setViewData(activeCurrency, data)
        }

        Log.d("Cata", "$TAG#startPresenting")
    }

    private fun subscribeToBookmarksListUpdates(currency: CurrencyRepresentation): Disposable {
        return bookmarksRepository.getBookmarksListObservable(currencyRepresentation = currency)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { coinList ->
                    //ensure content is visible
                    bookmarksView?.setContentVisible(isVisible = true)
                    Log.d("Cata", "Got new bookmarks! activeCurrency: $currency")
                    setViewData(currency, coinList.map { coin ->
                        coin.toBookmarksCoin(bookmarksRepository.isBookmarkLoading(coinSymbol = coin.symbol))
                    })
                }
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

    override fun coinSelected(cryptoCoin: BookmarksCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin.toBusinessLayerCoin())
    }

    override fun userPullToRefresh() {
        refreshBookmarks()
        bookmarksView?.hideLoadingIndicator()
    }

    override fun displayCurrencyChanged(newDisplayCurrency: SelectionItem) {
        Log.d("Cata", "$TAG#displayCurrencyChanged")
        if (activeCurrency.currency == newDisplayCurrency.value) {
            //the new selection is equal to the previous one. do nothing
            return
        }
        activeCurrency = CurrencyRepresentation.valueOf(newDisplayCurrency.value.toUpperCase())
        refreshActiveCurrencyForSelectionList(changeCurrencyDialogItems)
        //remove old subscription
        compositeDisposable.remove(bookmarksListDisposable)
        bookmarksListDisposable.dispose()
        //subscribe to updates for the new currency
        bookmarksListDisposable = subscribeToBookmarksListUpdates(currency = activeCurrency)
        compositeDisposable.add(bookmarksListDisposable)
        //hide the list while the list is being fetched
        bookmarksView?.setContentVisible(isVisible = false)
        //launch the request
        bookmarksRepository.refreshBookmarks(activeCurrency, Consumer { Log.d("CAta", "Error occured at bookmars:$it") })
        Log.d("Cata", "displayCurrencyChanged: selectionItem:${newDisplayCurrency.name}")
    }

    override fun selectedSnapshotChanged(newSnapshot: SelectionItem) {
        if (activeSnapshotValue == newSnapshot.value) {
            //the new selection is equal to the previous one. do nothing
            return
        }
        activeSnapshotValue = newSnapshot.value
        refreshSelectedSnapshot(changeSnapshotDialogItems)
        Log.d("Cata", "selectedSnapshotChanged: selectionItem:${newSnapshot.name}")
    }

    private fun refreshSelectedSnapshot(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == activeSnapshotValue
        }
    }

    private fun refreshBookmarks() {
        val disposable = Single.fromCallable { bookmarksRepository.getBookmarks() }
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.map { it.toBookmarksCoin(isLoading = true) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    bookmarksView?.setListData(activeCurrency, it)
                }
                .observeOn(Schedulers.io())
                .subscribe { bookmarks ->
                    bookmarksRepository.refreshBookmarksById(currencyRepresentation = activeCurrency,
                            bookmarks = bookmarks,
                            errorHandler = Consumer {
                                Log.d("Cata", "Error when fetching bookmarks. Error: $it")
                            })
                }
        compositeDisposable.add(disposable)
    }

    override fun changeCurrencyButtonPressed() {
        bookmarksView?.openChangeCurrencyDialog(changeCurrencyDialogItems)
    }

    override fun selectSnapshotButtonPressed() {
        bookmarksView?.openSelectSnapshotDialog(changeSnapshotDialogItems)
    }

    private fun setViewData(primaryCurrency: CurrencyRepresentation, data: List<BookmarksCoin>) {
        bookmarksView?.setListData(primaryCurrency, data)
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

    private fun ensureSnapshotDialogOptionsInitialised() {
        if (!::changeSnapshotDialogItems.isInitialized) {
            val snapshotOptions: List<SelectionItem> = resourceDecoder
                    .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.SNAPSHOTS)
            snapshotOptions.onEach {
                it.isActive = it.value == activeSnapshotValue
            }
            changeSnapshotDialogItems = snapshotOptions
        }
    }

    private fun refreshActiveCurrencyForSelectionList(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == activeCurrency.currency
        }
    }

    //END base presenter methods
    companion object {
        const val TAG = "BookmarksPresenter"
    }
}