package com.catalinj.cryptosmart.presentationlayer.features.widgets.scrolltotop.contract

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalinj on 21.01.2018.
 */
interface ScrollToTopWidgetContract {

    interface ScrollToTopWidgetPresenter : MvpPresenter<ScrollToTopWidgetPresenter, ScrollToTopWidgetView> {

        var itemPositionThreshold: Int

        fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int)

        fun scrollToTopPressed()
    }

    interface ScrollToTopWidgetView : MvpView<ScrollToTopWidgetPresenter, ScrollToTopWidgetView> {

        fun scrollTo(scrollPosition: Int)

        fun isScrollToTopVisible(): Boolean

        fun revealScrollToTopButton()

        fun hideScrollToTopButton()

        fun getDisplayedItemPosition(): Int
    }
}