package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        fun userPullToRefresh()

        fun setInitialInfo(coinName: String, coinSymbol: String, coinId: String, change1h: Float)

        fun getCoinId(): String

        fun getCoinSymbol(): String

        fun registerChild(coinInfoPresenter: CoinInfoContract.CoinInfoPresenter)

        fun registerChild(coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter)

        fun childStartedLoading()

        fun childFinishedLoading()

        fun updateChange1h(newChange1h: Float)
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView>, LoadingView {

        fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float)

        fun getActiveChildView(): MvpView<*, *>
    }
}