package com.catalinjurjiu.kairoscrypto.presentationlayer.common.view

import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter

/**
 * Base interface definition for a simple View intended to be used in a classical MVP architecture.
 *
 * Typically, this view will be managed by its associated [MvpPresenter].
 *
 * This View will always invoke the methods of its Presenter on the UI Thread.
 *
 * Created by catalinj on 21.01.2018.
 */
interface MvpView<P : MvpPresenter<P, V>, V : MvpView<P, V>> {

    /**
     * Called by the presenter when this view instance needs to initialize.
     */
    fun initialise()

    /**
     * Returns this view's presenter.
     */
    fun getPresenter(): P
}