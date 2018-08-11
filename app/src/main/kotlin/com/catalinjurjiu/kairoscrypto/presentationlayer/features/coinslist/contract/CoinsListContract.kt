package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.LoadingView
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

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
    }

    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        fun setListData(data: List<CryptoCoin>)

        fun refreshContent()

        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        fun setContentVisible(isVisible: Boolean)
    }
}