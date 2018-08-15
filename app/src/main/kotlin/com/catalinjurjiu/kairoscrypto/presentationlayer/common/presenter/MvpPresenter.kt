package com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter

import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

/**
 * Base interface definition for a Presenter intended to be used in a classical MVP architecture.
 *
 * Typically, this view will be managed by its associated [MvpView].
 *
 * This Presenter definition considers that the Presenter is active & that its view is visible &
 * focused after [startPresenting] has been invoked, until [stopPresenting] is invoked.
 *
 * [viewAvailable] is called as soon as a reference to the [MvpView] is available. When the view
 * is destroyed, [viewDestroyed] is called. Failing to call [viewDestroyed] will result in memory
 * leaks.
 *
 * During [viewAvailable], this Presenter will invoke [MvpView.initialise].
 *
 * Having this in mind, a lifecycle of the presenter would look in the following way:
 *
 * ```
 *                 create
 *
 *            viewAvailable(view)
 *
 *              startPresenting
 *
 *  <<Presenter is active & drives the view>>
 *
 *              stopPresenting
 *
 *              viewDestroyed
 *
 *           <<<garbage collected>>>
 * ```
 *
 * [viewAvailable] & [viewDestroyed] can be called several times after the Presenter has been created,
 * and until it has been destroyed. This usually happens if the view is swapped, or destroyed
 * temporarily and then recreated.
 *
 * Likewise, [startPresenting] & [stopPresenting] can also be called several times between two
 * [viewAvailable]/[viewDestroyed] calls.
 *
 * This Presenter will always invoke the methods of its view on the view's UI Thread.
 *
 * @see MvpView
 *
 * Created by catalinj on 21.01.2018.
 */
interface MvpPresenter<P : MvpPresenter<P, V>, V : MvpView<P, V>> {

    /**
     * Called when a reference to the [MvpView] is available.
     *
     * During this method, [MvpView.initialise] will be invoked on the view reference received as
     * parameter.
     *
     * @param view the MvpView to which will be managed by this Presenter.
     */
    fun viewAvailable(view: V)

    /**
     * Called when the Presenter needs to start managing the view.
     *
     * Typically, this is where the initial data load requests are started (or when the data refresh
     * requests are launched), or when various animations are started.
     */
    fun startPresenting()

    /**
     * Called when the Presenter needs to stop managing the view. Usually this means that the
     * view is no longer visible on screen (app in background, user is navigating to a different
     * screen), etc.
     *
     * This method being called does not imply that the view is going to be destroyed. [startPresenting]
     * can be called again, if the view regains focus (if the app comes back into foreground for
     * instance).
     */
    fun stopPresenting()

    /**
     * Called when the view has been destroyed. The view's reference should be release, to allow the
     * garbage collector the free the memory allocated for the view.
     *
     * The Presenter is still alive at this point. [viewAvailable] can be called at a later point &
     * provide a new view instance.
     */
    fun viewDestroyed()

    /**
     * Returns the view associated with this presenter.
     *
     * @return the [MvpView] managed by this Presenter. Can be `null` if invoked before [viewAvailable]
     * or after [viewDestroyed].
     */
    fun getView(): V?
}