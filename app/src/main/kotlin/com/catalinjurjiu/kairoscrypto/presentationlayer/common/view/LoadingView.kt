package com.catalinjurjiu.kairoscrypto.presentationlayer.common.view

/**
 * Interface definition for a view capable of showing & hiding a loading indicator.
 *
 * Created by catalin on 18/04/2018.
 */
interface LoadingView {

    /**
     * Shows the loading indicator.
     */
    fun showLoadingIndicator()

    /**
     * Hides the loading indicator.
     */
    fun hideLoadingIndicator()
}