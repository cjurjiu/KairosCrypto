package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 05/05/2018.
 */
interface CoinInfoContract {
    interface CoinInfoPresenter : MvpPresenter<CoinInfoPresenter, CoinInfoView> {

        fun setCoinId(coinId: String)

        fun setCoinSymbol(coinSymbol: String)

        fun handleRefresh(): Boolean

        fun getPrimaryCurrency(): CurrencyRepresentation
    }

    interface CoinInfoView : MvpView<CoinInfoPresenter, CoinInfoView> {

        fun setCoinInfo(coinDetails: CryptoCoinDetails)

        fun showError(errorCode: ErrorCode, retryHandler: () -> Unit)
    }
}