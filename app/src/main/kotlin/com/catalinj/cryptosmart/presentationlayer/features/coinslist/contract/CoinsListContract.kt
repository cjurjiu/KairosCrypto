package com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
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

        fun changeCurrencyPressed()

        fun selectSnapshotButtonPressed()

        fun getSelectedCurrency(): CurrencyRepresentation

        fun displayCurrencyChanged(newSelectedCurrency: SelectionItem)

        fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem)

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun userPullToRefresh()
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>)

        fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>)

        fun setListData(data: List<CryptoCoin>)

        fun scrollTo(scrollPosition: Int)
    }
}