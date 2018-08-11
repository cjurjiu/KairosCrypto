package com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin

/**
 * Component charged with navigation across various screens.
 *
 * Created by catalin on 27/04/2018.
 */
interface Navigator {

    /**
     * Opens the CoinsList screen.
     *
     * Clears the backstack prior to navigation to the coin list screen. This transaction is not
     * added to the backstack.
     */
    fun openCoinListScreen()

    /**
     * Opens the Bookmarks screen.
     *
     * Clears the backstack prior to navigation to the coin list screen. This transaction is not
     * added to the backtack.
     */
    fun openBookmarksScreen()


    /**
     * Opens the Settings screen.
     *
     * Clears the backstack prior to navigation to the coin list screen. This transaction is not
     * added to the backtack.
     */
    fun openSettingsScreen()

    /**
     * Opens the CoinDetails screen.
     *
     * @param cryptoCoin the serverId of the coin for which the details are being displayed
     */
    fun openCoinDetailsScreen(cryptoCoin: CryptoCoin)

    /**
     * Navigates back.
     *
     * @return ```true``` if the Navigator was able to navigate back, ```false``` otherwise.
     */
    fun navigateBack(): Boolean
}