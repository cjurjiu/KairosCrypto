package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 05/05/2018.
 */
interface CoinMarketsContract {

    interface CoinMarketsPresenter : MvpPresenter<CoinMarketsPresenter, CoinMarketsView> {

        fun handleRefresh(): Boolean

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun scrollToTopPressed()
    }

    interface CoinMarketsView : MvpView<CoinMarketsPresenter, CoinMarketsView> {

        fun setData(marketInfo: List<CryptoCoinMarketInfo>)

        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        //scroll to top behavior
        fun scrollTo(scrollPosition: Int)

        fun setContentVisible(isVisible: Boolean)

        fun isScrollToTopVisible(): Boolean

        fun revealScrollToTopButton()

        fun hideScrollToTopButton()

        fun getDisplayedItemPosition(): Int
    }
}