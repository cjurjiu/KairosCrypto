package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.BookmarksComponent
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.common.extension.toMessageResId
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.common.view.CryptoListAdapterSettings
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinj.cryptosmart.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.wheelbarrow.InjectorFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_bookmarks.view.*
import javax.inject.Inject

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksFragment : InjectorFragment<BookmarksComponent>(), BookmarksContract.BookmarksView,
        BackEventAwareComponent {

    override val name: String = TAG

    @Inject
    protected lateinit var bookmarksPresenter: BookmarksContract.BookmarksPresenter
    @Inject
    protected lateinit var imageHelper: ImageHelper<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: BookmarksListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var floatingScrollToTopButton: View
    private lateinit var optionsToolbar: CoinDisplayOptionsToolbar
    private var hideAnimationInProgress: Boolean = false


    //android fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookmarksPresenter.navigator = (activity as MainActivity).navigator
        (activity as MainActivity).showBottomNavigation()
        bookmarksPresenter.viewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
        bookmarksPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        bookmarksPresenter.stopPresenting()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.clearOnScrollListeners()
        bookmarksPresenter.viewDestroyed()
        //also notify the toolbar that the view is destroyed
        optionsToolbar.getPresenter().viewDestroyed()
        Log.d(TAG, "$TAG#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }
    //END android fragment lifecycle

    //mvp view methods
    override fun initialise() {
        Log.d("Cata", "$TAG#initialise")
        val view = view!!
        initToolbar(view, activity as AppCompatActivity)
        initRecyclerView(view)
        initSwipeRefreshLayout(view)
        this.floatingScrollToTopButton = view.button_floating_scroll_to_top
    }

    override fun getPresenter(): BookmarksContract.BookmarksPresenter {
        return bookmarksPresenter
    }

    override fun onBack(): Boolean {
        return false
    }
    //end mvp view methods

    //bookmarks view methods
    override fun setListData(bookmarksList: List<BookmarksCoin>) {
        recyclerViewAdapter.adapterSettings = recyclerViewAdapter.adapterSettings
                .updateCurrency(newCurrency = bookmarksPresenter.displayCurrency)
        recyclerViewAdapter.coins = bookmarksList.toMutableList()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun refreshContent() {
        recyclerViewAdapter.adapterSettings = CryptoListAdapterSettings(currency = bookmarksPresenter.displayCurrency,
                snapshot = bookmarksPresenter.displaySnapshot)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun showError(errorCode: ErrorCode, retryAction: () -> Unit) {
        SnackBarWrapper.showSnackBarWithAction(view = view!!,
                infoMessageRes = errorCode.toMessageResId(),
                actionMessageRes = R.string.cta_try_again,
                clickListener = View.OnClickListener {
                    //invoke the retry action
                    retryAction.invoke()
                })
    }

    override fun scrollTo(scrollPosition: Int) {
        recyclerView.scrollToPosition(scrollPosition)
    }

    override fun setContentVisible(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun isScrollToTopVisible(): Boolean = floatingScrollToTopButton.visibility == View.VISIBLE

    override fun revealScrollToTopButton() {
        val set = AnimatorInflater.loadAnimator(context, R.animator.animator_floating_action_button_reveal) as AnimatorSet
        floatingScrollToTopButton.visibility = View.VISIBLE
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                floatingScrollToTopButton.setOnClickListener {
                    bookmarksPresenter.scrollToTopPressed()
                }
                set.removeAllListeners()
            }
        })
        set.setTarget(floatingScrollToTopButton)
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
                floatingScrollToTopButton.setOnClickListener(null)
            }

            override fun onAnimationEnd(animation: Animator?) {
                floatingScrollToTopButton.visibility = View.GONE
                hideAnimationInProgress = false
                set.removeAllListeners()
            }
        })
        set.setTarget(floatingScrollToTopButton)
        set.start()
    }

    override fun getDisplayedItemPosition(): Int = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    //loading view methods
    override fun showLoadingIndicator() {
        //do nothing, each card displays its own loading indicator
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }
    //end bookmarks & loading view view methods

    private fun initSwipeRefreshLayout(view: View) {
        swipeRefreshLayout = view.swiperefreshlayout_bookmarks_list
        swipeRefreshLayout.setOnRefreshListener {
            bookmarksPresenter.userPullToRefresh()
        }
    }

    private fun initRecyclerView(view: View) {
        val adapterSettings = CryptoListAdapterSettings(currency = bookmarksPresenter.displayCurrency,
                snapshot = bookmarksPresenter.displaySnapshot)

        recyclerView = view.recyclerview_bookmarks_list
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerViewAdapter = BookmarksListAdapter(context = context!!,
                adapterSettings = adapterSettings,
                coins = mutableListOf(),
                imageHelper = imageHelper) {
            bookmarksPresenter.coinSelected(it)
        }
        recyclerView.adapter = recyclerViewAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                bookmarksPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }
        })
    }

    private fun initToolbar(rootView: View, appCompatActivity: AppCompatActivity) {
        Log.d("Cata", "have toolbar!")
        optionsToolbar = rootView.screen_toolbar
        appCompatActivity.setSupportActionBar(optionsToolbar)
        lifecycle.addObserver(optionsToolbar)
        //also inject the toolbar with the same injector
        injector.inject(optionsToolbar)
        //notify the toolbar's presenter that its view is available
        optionsToolbar.getPresenter().viewAvailable(optionsToolbar)
    }

    /**
     * Factory for this Fragment.
     */
    class Factory(val activityComponent: ActivityComponent) : InjectorFragmentFactory<BookmarksComponent>() {

        override fun onCreateFragment(): InjectorFragment<BookmarksComponent> {
            return BookmarksFragment()
        }

        override fun onCreateInjector(): BookmarksComponent {
            return activityComponent.getBookmarksComponent(BookmarksModule())
        }
    }

    companion object {
        const val TAG = "BookmarksView"
    }
}