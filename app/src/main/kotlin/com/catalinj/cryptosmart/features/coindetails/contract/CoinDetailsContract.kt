package com.catalinj.cryptosmart.features.coinslist.contract

import com.catalinj.cryptosmart.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.common.view.MvpView
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        fun userPressedBack(): Boolean

        fun userPullToRefresh()

        fun receivedFocus()
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView> {

        fun setCoinData(data: CoinMarketCapCryptoCoin)

        fun increaseValue()

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }
}