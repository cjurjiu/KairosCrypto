package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.view.controller.LoadingController
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.toBookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.toBusinessLayerCoin
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksPresenter(private val bookmarksRepository: BookmarksRepository,
                         private val userSettings: CryptoSmartUserSettings) : BookmarksContract.BookmarksPresenter {

    override var navigator: Navigator? = null
    private var loadController: LoadingController? = null
    private var primaryCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
    private var bookmarksView: BookmarksContract.BookmarksView? = null
    private val compositeDisposable = CompositeDisposable()
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
        val dataDisposable = bookmarksRepository.getBookmarksListObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { coinList ->
                    Log.d("Cata", "Got new bookmarks!")
                    setViewData(primaryCurrency, coinList.map { coin ->
                        coin.toBookmarksCoin(
                                bookmarksRepository.isBookmarkLoading(coinSymbol = coin.symbol))
                    })
                }

        compositeDisposable.add(loadingDisposable)
        compositeDisposable.add(dataDisposable)

        val data = availableData
        if (data == null) {
            refreshBookmarks()
        } else {
            setViewData(primaryCurrency, data)
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

    override fun coinSelected(cryptoCoin: BookmarksCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin.toBusinessLayerCoin())
    }

    override fun userPullToRefresh() {
        refreshBookmarks()
        bookmarksView?.hideLoadingIndicator()
    }

    private fun refreshBookmarks() {
        val disposable = Single.fromCallable { bookmarksRepository.getBookmarks() }
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.map { it.toBookmarksCoin(isLoading = true) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    bookmarksView?.setListData(primaryCurrency, it)
                }
                .observeOn(Schedulers.io())
                .subscribe { bookmarks ->
                    bookmarksRepository.refreshBookmarksById(currencyRepresentation = primaryCurrency,
                            bookmarks = bookmarks,
                            errorHandler = Consumer {
                                Log.d("Cata", "Error when fetching bookmarks. Error: $it")
                            })
                }
        compositeDisposable.add(disposable)
    }

    private fun setViewData(primaryCurrency: CurrencyRepresentation, data: List<BookmarksCoin>) {
        bookmarksView?.setListData(primaryCurrency, data)
    }

    //END base presenter methods
    companion object {
        const val TAG = "BookmarksPresenter"
    }
}