package com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        fun userPressedBack(): Boolean

        fun userPullToRefresh()
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView> {

        fun setCoinData(data: CoinMarketCapCryptoCoin)

        fun increaseValue()

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }
}