package com.catalinj.cryptosmart.features.coinslist.contract

import com.catalinj.cryptosmart.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.common.view.LoadingView
import com.catalinj.cryptosmart.common.view.MvpView
import com.catalinj.cryptosmart.features.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView> {

        fun coinSelected(position: Int)

        fun changeCurrencyPressed()

        fun sortListButtonPressed()

        fun selectSnapshotButtonPressed()

        fun displayCurrencyChanged(newSelectedCurrency: SelectionItem)

        fun listSortingChanged(newSortingOrder: SelectionItem)

        fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem)

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun userPullToRefresh()
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>)

        fun openSortListDialog(selectionItems: List<SelectionItem>)

        fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>)

        fun setListData(data: List<CoinMarketCapCryptoCoin>)

        fun scrollTo(scrollPosition: Int)
    }
}