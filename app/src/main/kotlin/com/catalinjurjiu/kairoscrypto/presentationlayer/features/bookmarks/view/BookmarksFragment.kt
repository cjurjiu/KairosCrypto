package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.di.components.ActivityComponent
import com.catalinjurjiu.kairoscrypto.di.components.BookmarksComponent
import com.catalinjurjiu.kairoscrypto.di.modules.bookmarks.BookmarksModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.MainActivity
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.toMessageResId
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.CryptoListAdapterSettings
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_bookmarks.view.*
import javax.inject.Inject

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksFragment : WheelbarrowFragment<BookmarksComponent>(),
        BookmarksContract.BookmarksView,
        BackEventAwareComponent {

    override val name: String = TAG

    @Inject
    protected lateinit var bookmarksPresenter: BookmarksContract.BookmarksPresenter
    @Inject
    protected lateinit var imageHelper: ImageHelper<String>

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: BookmarksListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var floatingScrollToTopButton: ScrollToTopFloatingActionButton
    private lateinit var optionsToolbar: CoinDisplayOptionsToolbar

    //android fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(this)
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
        //notify the toolbar that the view is destroyed
        optionsToolbar.getPresenter().viewDestroyed()
        //notify the floating scroll-to-top button that the view is destroyed
        floatingScrollToTopButton.getPresenter().viewDestroyed()
        Log.d(TAG, "$TAG#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }
    //END android fragment lifecycle

    //mvp view methods
    override fun initialise() {
        Log.d("Cata", "$TAG#initialise")
        val view = view!!
        initToolbar(view = view, appCompatActivity = activity as AppCompatActivity)
        initRecyclerView(view = view)
        initSwipeRefreshLayout(view = view)
        initFloatingActionButton(view = view)
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

    override fun setContentVisible(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

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
    }

    private fun initToolbar(view: View, appCompatActivity: AppCompatActivity) {
        Log.d("Cata", "have toolbar!")
        optionsToolbar = view.screen_toolbar
        appCompatActivity.setSupportActionBar(optionsToolbar)
        lifecycle.addObserver(optionsToolbar)
        //also inject the toolbar with the same injector
        cargo.inject(optionsToolbar)
        //notify the toolbar's presenter that its view is available
        optionsToolbar.getPresenter().viewAvailable(optionsToolbar)
    }

    private fun initFloatingActionButton(view: View) {
        floatingScrollToTopButton = view.button_floating_scroll_to_top
        cargo.inject(floatingScrollToTopButton)
        //call before notifying the presenter that the view is available
        floatingScrollToTopButton.setItemPositionThreshold(value = SCROLL_TO_TOP_LIST_THRESHOLD)
        floatingScrollToTopButton.setupWithViewRecyclerView(recyclerView = recyclerView)
        //finally tell the presenter that the view is ready to be used
        floatingScrollToTopButton.getPresenter().viewAvailable(floatingScrollToTopButton)
        //add the floating button as a lifecycle observer
        lifecycle.addObserver(floatingScrollToTopButton)
    }

    /**
     * Factory for this Fragment.
     */
    class Factory(val activityComponent: ActivityComponent) : WheelbarrowFragment.Factory<BookmarksComponent>() {

        override fun onCreateFragment(): WheelbarrowFragment<BookmarksComponent> {
            return BookmarksFragment()
        }

        override fun onCreateCargo(): BookmarksComponent {
            return activityComponent.getBookmarksComponent(BookmarksModule())
        }
    }

    companion object {
        const val TAG = "BookmarksView"
        private const val SCROLL_TO_TOP_LIST_THRESHOLD = 10
    }
}