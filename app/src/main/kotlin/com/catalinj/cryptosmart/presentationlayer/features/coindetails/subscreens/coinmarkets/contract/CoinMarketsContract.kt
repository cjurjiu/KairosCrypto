package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 05/05/2018.
 */
interface CoinMarketsContract {

    interface CoinMarketsPresenter : MvpPresenter<CoinMarketsPresenter, CoinMarketsView> {

        fun handleRefresh(): Boolean
    }

    interface CoinMarketsView : MvpView<CoinMarketsPresenter, CoinMarketsView> {
        //empty for now
    }
}