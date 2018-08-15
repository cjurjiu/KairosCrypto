package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

/**
 * Contract which defines the view-presenter interactions that occur in the CoinInfo screen.
 *
 * Created by catalin on 05/05/2018.
 */
interface CoinInfoContract {

    /**
     * Interface definition for a presenter in charge of the CoinInfo screen.
     */
    interface CoinInfoPresenter : MvpPresenter<CoinInfoPresenter, CoinInfoView> {

        /**
         * Sets the coin Id of the coin for which the information needs to be shown.
         *
         * @param coinId the unique coin id for the coin to be displayed.
         */
        fun setCoinId(coinId: String)

        /**
         * Sets the coin symbol of the coin for which the information needs to be shown.
         *
         * @param coinSymbol the symbol the coin to be displayed (e.g. BTC for Bitcoin).
         */
        fun setCoinSymbol(coinSymbol: String)

        /**
         * Performs a data refresh.
         */
        fun handleRefresh(): Boolean

        /**
         * Returns the [CurrencyRepresentation] regarded as being the primary currency.
         *
         * @return the currency regarded as being the primary
         */
        fun getPrimaryCurrency(): CurrencyRepresentation
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * CoinInfo screen.
     */
    interface CoinInfoView : MvpView<CoinInfoPresenter, CoinInfoView> {

        /**
         * Sets the coin details data to be consumed by this view.
         *
         * @param coinDetails the data to be displayed by the view.
         */
        fun setCoinInfo(coinDetails: CryptoCoinDetails)

        /**
         * Shows an error message to the user.
         *
         * @param errorCode the error code associated with the error that has occurred
         * @param retryHandler the handler to be invoked when the user attempts to retry the failed
         * action.
         */
        fun showError(errorCode: ErrorCode, retryHandler: () -> Unit)
    }
}