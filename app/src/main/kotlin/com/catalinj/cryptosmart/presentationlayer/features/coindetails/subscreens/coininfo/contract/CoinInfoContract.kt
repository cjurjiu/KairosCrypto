package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 05/05/2018.
 */
interface CoinInfoContract {
    interface CoinInfoPresenter : MvpPresenter<CoinInfoPresenter, CoinInfoView> {
        //empty for now
    }

    interface CoinInfoView : MvpView<CoinInfoPresenter, CoinInfoView> {
        //empty for now
    }
}