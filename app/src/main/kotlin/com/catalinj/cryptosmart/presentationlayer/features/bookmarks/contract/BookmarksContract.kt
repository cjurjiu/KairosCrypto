package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {
    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView> {

    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView> {

    }
}