package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        fun userPullToRefresh()

        fun setInitialInfo(coinName: String, coinSymbol: String, coinId: String, change1h: Float)

        fun getCoinId(): String

        fun getCoinSymbol(): String
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView> {

        fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float)

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }
}