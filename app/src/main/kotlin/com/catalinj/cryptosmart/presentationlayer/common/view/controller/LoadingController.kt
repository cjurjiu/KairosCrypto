package com.catalinj.cryptosmart.presentationlayer.common.view.controller

import android.os.Handler
import android.os.Looper
import com.catalinj.cryptosmart.presentationlayer.common.view.LoadingView

/**
 * Contains logic which can drive a [LoadingView] that will be visible for
 * a minimum amount of time to avoid "flashes" in the UI when an event could take
 * a largely variable time to complete (from none, to a user perceivable amount).
 *
 * Logic is largely taken from: android.support.v4.widget.ContentLoadingProgressBar
 *
 * Created by catalin on 18/04/2018.
 */
class LoadingController(private val loadingView: LoadingView) {

    private var mStartTime: Long = -1

    private var mPostedHide = false

    private var mDismissed = false

    private val handler = Handler(Looper.getMainLooper())

    private val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        loadingView.hideLoadingIndicator()
    }

    /**
     * Cleans up the resources used by this component.
     */
    fun cleanUp() {
        handler.removeCallbacks(mDelayedHide)
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @Synchronized
    fun hide() {
        mDismissed = true
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= Companion.MIN_SHOW_TIME || mStartTime == -1L) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            loadingView.hideLoadingIndicator()
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                handler.postDelayed(mDelayedHide, Companion.MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @Synchronized
    fun show() {
        // Reset the start time.
        mDismissed = false
        handler.removeCallbacks(mDelayedHide)
        mStartTime = System.currentTimeMillis()
        mPostedHide = false
        loadingView.showLoadingIndicator()
    }

    private companion object {
        const val MIN_SHOW_TIME = 1000 // ms
    }
}