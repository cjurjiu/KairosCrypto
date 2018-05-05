package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        fun userPressedBack(): Boolean

        fun userPullToRefresh()

        fun setCoinId(coinId: String)
    }

    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView> {

        fun setCoinData(coinDetails: CryptoCoinDetails)

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }
}