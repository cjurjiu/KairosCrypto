package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

import com.catalinj.cryptosmart.businesslayer.model.BookmarksCoin
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

/**
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {

    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView>,
            CoinsDisplayOptionsContract.CoinDisplayController {

        var navigator: Navigator?

        fun coinSelected(cryptoCoin: BookmarksCoin)

        fun userPullToRefresh()
    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView>, LoadingView {

        fun setListData(bookmarksList: List<BookmarksCoin>)

        fun refreshContent()

        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        fun setContentVisible(isVisible: Boolean)
    }
}