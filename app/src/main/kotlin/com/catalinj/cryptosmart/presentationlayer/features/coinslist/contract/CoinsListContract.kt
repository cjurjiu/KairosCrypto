package com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView>,
            CoinsDisplayOptionsContract.CoinDisplayController {

        var navigator: Navigator?

        fun coinSelected(selectedCoin: CryptoCoin)

        fun userPullToRefresh()

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun scrollToTopPressed()
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun setListData(data: List<CryptoCoin>)

        fun refreshContent()

        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        fun setContentVisible(isVisible: Boolean)

        fun scrollTo(scrollPosition: Int)

        fun isScrollToTopVisible(): Boolean

        fun revealScrollToTopButton()

        fun hideScrollToTopButton()

        fun getDisplayedItemPosition(): Int

    }
}