package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.LoadingView
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

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