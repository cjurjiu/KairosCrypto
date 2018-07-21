package com.catalinj.cryptosmart.presentationlayer.features.widgets.scrolltotop.presenter

import com.catalinj.cryptosmart.presentationlayer.features.widgets.scrolltotop.contract.ScrollToTopWidgetContract

class ScrollToTopWidgetPresenter : ScrollToTopWidgetContract.ScrollToTopWidgetPresenter {

    private var view: ScrollToTopWidgetContract.ScrollToTopWidgetView? = null
    override var itemPositionThreshold: Int = 10

    //mvp presenter methods
    override fun startPresenting() {
        //nothing so far
    }

    override fun stopPresenting() {
        //nothing so far
    }

    override fun viewAvailable(view: ScrollToTopWidgetContract.ScrollToTopWidgetView) {
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        this.view = null
    }

    override fun getView(): ScrollToTopWidgetContract.ScrollToTopWidgetView? = view
    //end mvp presenter methods

    //scroll to top presenter methods
    override fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int) {
        //scroll to top button hide/reveal logic
        val displayedItemPosition = (view?.getDisplayedItemPosition() ?: 0)
        val scrollToTopVisible = view?.isScrollToTopVisible() ?: false
        if (displayedItemPosition > itemPositionThreshold && !scrollToTopVisible) {
            view?.revealScrollToTopButton()
        } else if (displayedItemPosition < itemPositionThreshold && scrollToTopVisible) {
            view?.hideScrollToTopButton()
        }
    }

    override fun scrollToTopPressed() {
        view?.scrollTo(0)
        view?.hideScrollToTopButton()
    }
    //end scroll to top presenter methods

}