package com.catalinj.cryptosmart.features.coinslist.contract

import com.catalinj.cryptosmart.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.common.view.LoadingView
import com.catalinj.cryptosmart.common.view.MvpView
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView> {

        fun coinSelected(position: Int)

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun userPullToRefresh()
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun setListData(data: List<CoinMarketCapCryptoCoin>)

        fun scrollTo(scrollPosition: Int)
    }
}