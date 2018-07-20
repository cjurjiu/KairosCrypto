package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.businesslayer.model.PredefinedSnapshot
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

/**
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {

    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView> {

        var navigator: Navigator?

        fun coinSelected(cryptoCoin: BookmarksCoin)

        fun userPullToRefresh()

        fun scrollToTopPressed()

        fun changeCurrencyButtonPressed()

        fun selectSnapshotButtonPressed()

        fun displayCurrencyChanged(newDisplayCurrency: SelectionItem)

        fun selectedSnapshotChanged(newSnapshot: SelectionItem)

        fun getSelectedCurrency(): CurrencyRepresentation

        fun getSelectedSnapshot(): PredefinedSnapshot
        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)
    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView>, LoadingView {

        fun setListData(bookmarksList: List<BookmarksCoin>)

        fun refreshContent()

        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        fun scrollTo(scrollPosition: Int)

        fun setContentVisible(isVisible: Boolean)

        fun isScrollToTopVisible(): Boolean

        fun revealScrollToTopButton()

        fun hideScrollToTopButton()

        fun getDisplayedItemPosition(): Int

        fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>)

        fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>)
    }
}