package com.catalinj.cryptosmart.features.coinslist.contract

import com.catalinj.cryptosmart.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.common.view.MvpView
import com.catalinj.cryptosmart.network.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView> {

        fun coinSelected(position: Int)

        fun userPullToRefresh()
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView> {

        fun setListData(data: List<CoinMarketCapCryptoCoin>)

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }
}