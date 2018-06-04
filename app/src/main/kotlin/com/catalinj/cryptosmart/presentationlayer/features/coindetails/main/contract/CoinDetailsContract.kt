package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract

import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
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
        var navigator: Navigator?

        fun userPullToRefresh()

        fun getCoinData(): CoinDetailsPartialData

        fun registerChild(coinInfoPresenter: CoinInfoContract.CoinInfoPresenter)

        fun registerChild(coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter)

        fun childStartedLoading()

        fun childFinishedLoading()

        fun updateChange1h(newChange1h: Float)

        fun userPressedBack()

        data class CoinDetailsPartialData(val coinName: String,
                                          val webFriendlyName: String,
                                          val coinSymbol: String,
                                          val coinId: String,
                                          val change1h: Float)
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView>, LoadingView {

        fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float)

        fun getActiveChildView(): MvpView<*, *>
    }
}