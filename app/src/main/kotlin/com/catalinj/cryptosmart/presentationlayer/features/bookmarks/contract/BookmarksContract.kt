package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin

/**
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {
    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView> {
        var navigator: Navigator?

        fun coinSelected(cryptoCoin: BookmarksCoin)

        fun userPullToRefresh()
    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView>, LoadingView {
        fun setListData(primaryCurrency: CurrencyRepresentation, bookmarksList: List<BookmarksCoin>)
    }
}