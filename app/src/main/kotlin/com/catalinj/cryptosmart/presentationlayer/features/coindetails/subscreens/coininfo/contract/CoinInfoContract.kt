package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 05/05/2018.
 */
interface CoinInfoContract {
    interface CoinInfoPresenter : MvpPresenter<CoinInfoPresenter, CoinInfoView> {

        fun setCoinId(coinId: String)

        fun setCoinSymbol(coinSymbol: String)

        fun handleRefresh(): Boolean
    }

    interface CoinInfoView : MvpView<CoinInfoPresenter, CoinInfoView> {

        fun setCoinInfo(coinDetails: CryptoCoinDetails)
    }
}