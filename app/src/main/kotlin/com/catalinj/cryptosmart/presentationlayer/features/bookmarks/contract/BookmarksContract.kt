package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

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

        fun displayCurrencyChanged(newDisplayCurrency: SelectionItem)

        fun selectedSnapshotChanged(newSnapshot: SelectionItem)

        fun changeCurrencyButtonPressed()

        fun selectSnapshotButtonPressed()

        fun getSelectedCurrency(): CurrencyRepresentation

        fun getSelectedSnapshot(): PredefinedSnapshot
    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView>, LoadingView {

        fun setListData(bookmarksList: List<BookmarksCoin>)

        fun refreshContent()

        fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>)

        fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>)

        fun setContentVisible(isVisible: Boolean)
    }
}