package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract

import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.LoadingView
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract

/**
 * Contract which defines the view-presenter interactions that occur in the Coin Details screen.
 *
 * The Coin Details displays besides its content, the content of two other child sub-screens:
 *   * the `CoinInfo` screen described by the [CoinInfoContract];
 *   * the `CoinMarkets` screen described by the [CoinMarketsContract].
 *
 * See the [CoinDetailsPresenter] & [CoinDetailsView] documentation for more detailed descriptions
 * in regards to the capabilities of the `CoinDetails` screen.
 *
 * Created by catalinj on 21.01.2018.
 */
interface CoinDetailsContract {

    /**
     * Interface definition for a presenter in charge of the CoinDetails screen.
     *
     * The `CoinDetails` screen hosts two sub-screens:
     *   * `CoinInfo` which displays detailed information about a coin;
     *   * `CoinMarkets` which displays the markets on which the coin can be traded.
     *
     * This presenter is aware of the existence of the [CoinInfoPresenter][CoinInfoContract.CoinInfoPresenter]
     * & [CoinMarketsPresenter][CoinMarketsContract.CoinMarketsPresenter]. After the `CoinInfoPresenter`
     * & `CoinMarketsPresenter` have been initialised, they will need to register
     * themselves with their parent (this [CoinDetailsPresenter]. When one of the child presenters
     * will need to register itself, it will call the appropriate [registerChild] method on their parent
     * instance of this class(`CoinDetailsPresenter`).
     *
     * This Presenter is also aware whether data is loading in any of its child presenters. Whenever
     * a child start loading data, [childStartedLoading] is invoked on the instance of this class.
     * Similarly, when a data load finishes in one of the child presenters, [childFinishedLoading] is
     * invoked.
     *
     * Furthermore, when the user requests a refresh via a pull-to-refresh event, this presenter is
     * the first one to get the event from the [CoinDetailsView], and is responsible of forwarding
     * (or not) the event to its children.
     *
     * In terms of navigation, that's also handled by this presenter via its [navigator] instance &
     * the [userPressedBack] method. The child sub-screens have do not participate in navigation as of
     * now.
     *
     * The majority of data is displayed in the child sub-screens, however the `CoinDetails` screen
     * itself also displays some *partial* coin data. Namely, this screen only displays the icon,
     * name, the 1H trend line for the selected coin, and whether a coin is marked as a bookmark or
     * not. It also allows to set/unset the selected coin as a bookmark by tapping the bookmark button.
     *
     * Errors are handled by each child independently.
     */
    interface CoinDetailsPresenter : MvpPresenter<CoinDetailsPresenter, CoinDetailsView> {

        /**
         * The [Navigator] instance used by this presenter when navigation to a different screen is
         * required.
         *
         * If it relies on platform-specific components with limited lifecycle (i.e. Activity context),
         * then make sure to deallocate and/or update the navigator when needed, to prevent leaks.
         */
        var navigator: Navigator?

        /**
         * Called by the view when the user pressed a button which triggers a back navigation.
         */
        fun userPressedBack()

        /**
         * Returns the stored [CoinDetailsPartialData], to display in the CoinDetails screen.
         *
         * @return a [CoinDetailsPartialData] instance to consume/display.
         */
        fun getCoinData(): CoinDetailsPartialData

        /**
         * Typically called by a child screen to update the trend line, after it finishes loading new
         * data.
         *
         * @param newChange1h the new 1 hour change for the selected coin, in percents.
         */
        fun updateChange1h(newChange1h: Float)

        /**
         * Invoked by the view whenever the clicks the toggle bookmark button.
         *
         * @param isChecked `true` if the coin should be saved as a bookmark, `false` otherwise
         */
        fun bookmarksCheckButtonPressed(isChecked: Boolean)

        //child registration
        /**
         * Called by the [CoinInfoPresenter][CoinInfoContract.CoinInfoPresenter] after it has been
         * initialised, to register itself to this presenter.
         */
        fun registerChild(coinInfoPresenter: CoinInfoContract.CoinInfoPresenter)

        /**
         * Called by the [CoinMarketsPresenter][CoinMarketsContract.CoinMarketsPresenter] after it has been
         * initialised, to register itself to this presenter.
         */
        fun registerChild(coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter)

        //data loading
        /**
         * Notifies that the user requested a data refresh via a "pull-to-refresh" gesture.
         */
        fun userPullToRefresh()

        /**
         * Notifies this presenter that one of the child screens has started a data load.
         */
        fun childStartedLoading()

        /**
         * Notifies this presenter that one of the child screens has stopped/finished a data load.
         */
        fun childFinishedLoading()

        /**
         * Immutable data model which stores a part of the data displayed in the CoinDetails screen.
         *
         * @property coinName the name of the displayed coin (e.g. Nano)
         * @property webFriendlyName the web-friendly name of the coin (e.g. nano)
         * @property coinSymbol the symbol of the coin (i.e. NANO, or BTC)
         * @property coinId the unique coin ID
         * @property change1h the change in value in the past hour, in percents
         */
        data class CoinDetailsPartialData(val coinName: String,
                                          val webFriendlyName: String,
                                          val coinSymbol: String,
                                          val coinId: String,
                                          val change1h: Float)
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * CoinDetails screen.
     *
     * This view itself acts more like a container for 2 sub-views: [CoinInfoView][CoinInfoContract.CoinInfoView],
     * [CoinMarketsView][CoinMarketsContract.CoinMarketsView].
     *
     * The 2 views cannot be both showed at the same time. To get the active child view, call [getActiveChildView].
     *
     * This view displays some data of the selected coin, but the data displayed by this view alone is
     * alone. Most of the relevant data is displayed by the two sub-views.
     */
    interface CoinDetailsView : MvpView<CoinDetailsPresenter, CoinDetailsView>, LoadingView {

        /**
         * Sets the partial data to be displayed by this view.
         *
         * @param coinName the name of the coin (e.g Bitcoin)
         * @param coinSymbol the symbol (e.g. BTC)
         * @param change1h the change in value over the previous hour, as a percent
         */
        fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float)

        /**
         * Identifies and returns the active child view.
         *
         * @return the active sub-view. Currently can an instance of [CoinInfoView][CoinInfoContract.CoinInfoView]
         * or of [CoinMarketsView][CoinMarketsContract.CoinMarketsView].
         */
        fun getActiveChildView(): MvpView<*, *>

        /**
         * Updates the display of the bookmarks button.
         *
         * @param isBookmark `true` if the button should indicate that the coin is a bookmark, `false`
         * otherwise
         */
        fun refreshBookmarkToggleButton(isBookmark: Boolean)
    }
}