package com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.contract.ScrollToTopWidgetContract
import javax.inject.Inject

class ScrollToTopFloatingActionButton @JvmOverloads constructor(context: Context?,
                                                                attrs: AttributeSet? = null,
                                                                defStyleAttr: Int = 0) :
        FloatingActionButton(context, attrs, defStyleAttr),
        ScrollToTopWidgetContract.ScrollToTopWidgetView,
        DefaultLifecycleObserver {

    @Inject
    protected lateinit var scrollToTopPresenter: ScrollToTopWidgetContract.ScrollToTopWidgetPresenter

    private lateinit var recyclerView: RecyclerView
    private var hideAnimationInProgress: Boolean = false

    //android lifecycle methods
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d("Cata", "ScrollToTopFloatingActionButton: onStart")
        scrollToTopPresenter.startPresenting()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("Cata", "ScrollToTopFloatingActionButton: onStop")
        scrollToTopPresenter.stopPresenting()
    }
    //end android lifecycle methods

    //mvp view methods
    override fun initialise() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollToTopPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }
        })
    }

    override fun getPresenter(): ScrollToTopWidgetContract.ScrollToTopWidgetPresenter = scrollToTopPresenter
    //end mvp view methods

    override fun scrollTo(scrollPosition: Int) {
        recyclerView.scrollToPosition(scrollPosition)
    }

    override fun isScrollToTopVisible(): Boolean = (this.visibility == View.VISIBLE)

    override fun revealScrollToTopButton() {
        val set = AnimatorInflater.loadAnimator(context, R.animator.animator_floating_action_button_reveal) as AnimatorSet
        this.visibility = View.VISIBLE
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                this@ScrollToTopFloatingActionButton.setOnClickListener {
                    scrollToTopPresenter.scrollToTopPressed()
                }
                set.removeAllListeners()
            }
        })
        set.setTarget(this)
        set.start()
    }

    override fun hideScrollToTopButton() {
        if (hideAnimationInProgress) {
            //guard to not start multiple hide animations...
            return
        }
        this.hideAnimationInProgress = true
        val set = AnimatorInflater.loadAnimator(context, R.animator.animator_floating_action_button_hide) as AnimatorSet
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                this@ScrollToTopFloatingActionButton.setOnClickListener(null)
            }

            override fun onAnimationEnd(animation: Animator?) {
                this@ScrollToTopFloatingActionButton.visibility = View.GONE
                hideAnimationInProgress = false
                set.removeAllListeners()
            }
        })
        set.setTarget(this)
        set.start()
    }

    //TODO@Catalin - add support for other types of layout managers
    override fun getDisplayedItemPosition(): Int = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    //end scroll to top view methods

    fun setupWithViewRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    fun getItemPositionThreshold() = scrollToTopPresenter.itemPositionThreshold

    fun setItemPositionThreshold(value: Int) {
        scrollToTopPresenter.itemPositionThreshold = value
    }
}