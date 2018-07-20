package com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.businesslayer.model.PredefinedSnapshot
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView> {
        var navigator: Navigator?

        fun coinSelected(selectedCoin: CryptoCoin)

        fun userPullToRefresh()

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun scrollToTopPressed()

        fun changeCurrencyButtonPressed()

        fun selectSnapshotButtonPressed()

        fun displayCurrencyChanged(newSelectedCurrency: SelectionItem)

        fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem)

        fun getSelectedCurrency(): CurrencyRepresentation

        fun getSelectedSnapshot(): PredefinedSnapshot
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun setListData(data: List<CryptoCoin>)

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