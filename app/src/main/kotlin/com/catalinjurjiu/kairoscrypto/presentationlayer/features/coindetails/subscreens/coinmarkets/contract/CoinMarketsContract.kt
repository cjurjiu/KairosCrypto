package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

/**
 * Contract which defines the view-presenter interactions that occur in the Coin Markets screen.
 *
 * Created by catalin on 05/05/2018.
 */
interface CoinMarketsContract {

    /**
     * Interface definition for a presenter in charge of the Coin List screen.
     */
    interface CoinMarketsPresenter : MvpPresenter<CoinMarketsPresenter, CoinMarketsView> {

        /**
         * Performs a data refresh.
         */
        fun handleRefresh(): Boolean

        //todo remove, replace with scrolltotop widget
        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun scrollToTopPressed()
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * CoinMarkets screen.
     *
     * Displays a list of Markets on which a specific crypto currency can be traded.
     */
    interface CoinMarketsView : MvpView<CoinMarketsPresenter, CoinMarketsView> {

        /**
         * Sets the market data that this view needs to display.
         *
         * @param marketInfo the list of [CryptoCoinMarketInfo] items that need to be displayed
         */
        fun setData(marketInfo: List<CryptoCoinMarketInfo>)

        /**
         * Shows an error message to the user.
         *
         * @param errorCode the error code associated with the error that has occurred
         * @param retryAction the handler to be invoked when the user attempts to retry the failed
         * action.
         */
        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        //todo remove, replace with scrolltotop widget
        //scroll to top behavior
        fun scrollTo(scrollPosition: Int)

        fun setContentVisible(isVisible: Boolean)

        fun isScrollToTopVisible(): Boolean

        fun revealScrollToTopButton()

        fun hideScrollToTopButton()

        fun getDisplayedItemPosition(): Int
    }
}