package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter

import android.util.Log
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksPresenter : BookmarksContract.BookmarksPresenter {
    private var bookmarksView: BookmarksContract.BookmarksView? = null
    //base presenter methods
    override fun startPresenting() {
        Log.d("Cata", "$TAG#startPresenting")
    }

    override fun stopPresenting() {
        Log.d("Cata", "$TAG#stopPresenting")
    }

    override fun viewAvailable(view: BookmarksContract.BookmarksView) {
        Log.d("Cata", "$TAG#viewAvailable")
        bookmarksView = view
        view.initialise()
    }

    override fun viewDestroyed() {
        Log.d("Cata", "$TAG#viewDestroyed")
    }

    override fun getView(): BookmarksContract.BookmarksView? {
        Log.d("Cata", "$TAG#getView")
        return bookmarksView
    }

    //END base presenter methods
    companion object {
        const val TAG = "BookmarksPresenter"
    }
}