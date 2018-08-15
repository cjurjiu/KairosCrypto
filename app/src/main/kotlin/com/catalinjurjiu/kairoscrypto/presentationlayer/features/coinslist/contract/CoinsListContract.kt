package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.LoadingView
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

/**
 * Contract which defines the view-presenter interactions that occur in the Coin List screen.
 *
 * Created by catalinj on 21.01.2018.
 */
interface CoinsListContract {

    /**
     * Interface definition for a presenter in charge of the Coin List screen.
     *
     * This presenter is also a [CoinDisplayController][CoinsDisplayOptionsContract.CoinDisplayController],
     * meaning it is aware of the fact that the list of coins can be displayed in various currencies,
     * or using different snapshots.
     */
    interface CoinsListPresenter : MvpPresenter<CoinsListPresenter, CoinsListView>,
            CoinsDisplayOptionsContract.CoinDisplayController {

        /**
         * The [Navigator] instance used by this presenter when navigation to a different screen is
         * required.
         *
         * If it relies on platform-specific components with limited lifecycle (i.e. Activity context),
         * then make sure to deallocate and/or update the navigator when needed, to prevent leaks.
         */
        var navigator: Navigator?

        /**
         * Notifies the presenter that the user has selected a coin.
         *
         * @param selectedCoin the coin selected by the user.
         */
        fun coinSelected(selectedCoin: CryptoCoin)

        /**
         * Notifies that the user requested a data refresh via a "pull-to-refresh" gesture.
         */
        fun userPullToRefresh()

        /**
         * Notifies that the contents of the view has been scrolled.
         *
         * @param currentScrollPosition an integer of arbitrary value which indicated the current
         * scroll positions. This value only makes sense together with [maxScrollPosition], and for
         * the current view instance.
         * @param maxScrollPosition the current max scroll value available for this view. does not
         * make sens if the view instance is changed/destroyed.
         */
        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * Coins List screen.
     *
     * This view is also a [LoadingView].
     */
    interface CoinsListView : MvpView<CoinsListPresenter, CoinsListView>, LoadingView {

        /**
         * Sets the coinList that this view needs to display.
         *
         * @param coinList the list of [CryptoCoin] items that need to be displayed
         */
        fun setListData(coinList: List<CryptoCoin>)

        /**
         * Re-queries the [CoinsListPresenter] for data, then redraws the UI.
         */
        fun refreshContent()

        /**
         * Shows an error message to the user.
         *
         * @param errorCode the error code associated with the error that has occurred
         * @param retryAction the handler to be invoked when the user attempts to retry the failed
         * action.
         */
        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        /**
         * Hides or reveals the view's content (all content, or only partial - view specific).
         *
         * @param isVisible `true` if the content should be visible, `false` otherwise
         */
        fun setContentVisible(isVisible: Boolean)
    }
}