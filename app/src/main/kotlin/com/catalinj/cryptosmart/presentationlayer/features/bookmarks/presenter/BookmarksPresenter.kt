package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksPresenter(val cryptoSmartDb: CryptoSmartDb,
                         val userSettings: CryptoSmartUserSettings) : BookmarksContract.BookmarksPresenter {
    override var navigator: Navigator? = null
    private var primaryCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()
    private var bookmarksView: BookmarksContract.BookmarksView? = null
    private val compositeDisposable = CompositeDisposable()

    //base presenter methods
    override fun startPresenting() {
        val disposable = Single.fromCallable {
            cryptoSmartDb.getBookmarksDao().getBookmarkedCoins()
        }
                .subscribeOn(Schedulers.io())
                .map { list ->
                    list.map { it.toBusinessLayerCoin() }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    Log.d("Cata", "$TAG#received data. setting it on view")
                    bookmarksView?.setListData(primaryCurrency, data)
                }
        compositeDisposable.add(disposable)

        Log.d("Cata", "$TAG#startPresenting")
    }

    override fun stopPresenting() {
        Log.d("Cata", "$TAG#stopPresenting")
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: BookmarksContract.BookmarksView) {
        Log.d("Cata", "$TAG#viewAvailable")
        bookmarksView = view
        view.initialise()
    }

    override fun viewDestroyed() {
        Log.d("Cata", "$TAG#viewDestroyed")
        navigator = null
    }

    override fun getView(): BookmarksContract.BookmarksView? {
        Log.d("Cata", "$TAG#getView")
        return bookmarksView
    }

    override fun coinSelected(cryptoCoin: CryptoCoin) {
        navigator?.openCoinDetailsScreen(cryptoCoin)
    }

    //END base presenter methods
    companion object {
        const val TAG = "BookmarksPresenter"
    }
}