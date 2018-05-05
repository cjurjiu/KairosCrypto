package com.catalinj.cryptosmart.presentationlayer.common.navigation

/**
 * Component charged with navigation across various screens.
 *
 * Created by catalin on 27/04/2018.
 */
interface Navigator {

    /**
     * Opens the coin list screen.
     *
     * Clears the backstack prior to navigation to the coin list screen.
     */
    fun openCoinListScreen()

    /**
     * Opens the coin details screen.
     *
     * @param cryptoCoinId the id of the coin for which the details are being displayed
     */
    fun openCoinDetailsScreen(cryptoCoinId: String)

    /**
     * Navigates back.
     *
     * @return ```true``` if the Navigator was able to navigate back, ```false``` otherwise.
     */
    fun navigateBack(): Boolean
}