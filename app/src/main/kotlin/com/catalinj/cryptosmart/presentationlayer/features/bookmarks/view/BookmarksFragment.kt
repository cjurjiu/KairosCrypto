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
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.view.OnItemSelectedListener
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.view.SelectionDialog
import com.catalinj.cryptosmart.presentationlayer.features.snackbar.SnackBarWrapper
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
    private var hideAnimationInProgress: Boolean = false
    private val onChangeCurrencyButtonClickedListener = View.OnClickListener { bookmarksPresenter.changeCurrencyButtonPressed() }
    private val onSnapshotButtonClickedListener = View.OnClickListener { bookmarksPresenter.selectSnapshotButtonPressed() }

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
        (activity as MainActivity).showBottomNavigation()
        bookmarksPresenter.navigator = (activity as MainActivity).navigator
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
    }
    //END android fragment lifecycle

    //mvp view methods
    override fun initialise() {
        Log.d("Cata", "$TAG#initialise")
        val view = view!!
        initSwipeRefreshLayout(view)
        initRecyclerView(view)
        this.floatingScrollToTopButton = view.button_floating_scroll_to_top
        //      rebind dialog
        SelectionDialog.rebindActiveDialogListeners(fragmentManager = fragmentManager!!,
                possibleDialogIdentifiers = BookmarksSelectionDialogType.children(),
                listenerFactory = ::getListenerForDialogType)
        initToolbar(view, activity as AppCompatActivity)
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
                .updateCurrency(newCurrency = bookmarksPresenter.getSelectedCurrency())
        recyclerViewAdapter.coins = bookmarksList.toMutableList()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun refreshContent() {
        recyclerViewAdapter.adapterSettings = CryptoListAdapterSettings(currency = bookmarksPresenter.getSelectedCurrency(),
                snapshot = bookmarksPresenter.getSelectedSnapshot())
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>) {
        SelectionDialog.showCancelable(dialogIdentifier = BookmarksSelectionDialogType.ChangeCurrency,
                fragmentManager = fragmentManager,
                data = selectionItems,
                listenerFactory = ::getListenerForDialogType)
    }

    override fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>) {
        SelectionDialog.showCancelable(dialogIdentifier = BookmarksSelectionDialogType.SelectSnapshot,
                fragmentManager = fragmentManager,
                data = selectionItems,
                listenerFactory = ::getListenerForDialogType)
    }

    override fun showError(errorCode: ErrorCode, retryAction: () -> Unit) {
        SnackBarWrapper.showSnackBar(view = view!!,
                infoMessage = errorCode.toMessageResId(),
                actionButtonMessage = R.string.cta_try_again,
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

    override fun showLoadingIndicator() {
        //do nothing, each card displays its own loading indicator
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }

    //end bookmarks view methods
    class Factory(val activityComponent: ActivityComponent) : InjectorFragmentFactory<BookmarksComponent>() {

        override fun onCreateFragment(): InjectorFragment<BookmarksComponent> {
            return BookmarksFragment()
        }

        override fun onCreateInjector(): BookmarksComponent {
            return activityComponent.getBookmarksComponent(BookmarksModule())
        }
    }

    private fun initSwipeRefreshLayout(view: View) {
        swipeRefreshLayout = view.swiperefreshlayout_bookmarks_list
        swipeRefreshLayout.setOnRefreshListener {
            bookmarksPresenter.userPullToRefresh()
        }
    }

    private fun initRecyclerView(view: View) {
        val adapterSettings = CryptoListAdapterSettings(currency = bookmarksPresenter.getSelectedCurrency(),
                snapshot = bookmarksPresenter.getSelectedSnapshot())

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
        val toolbar: Toolbar = rootView.findViewById(R.id.my_toolbar)
        appCompatActivity.setSupportActionBar(toolbar)

        //set the change currency button clicked listener
        val changeCurrencyButton = toolbar.findViewById<ImageButton>(R.id.button_change_currency)
        changeCurrencyButton?.setOnClickListener(onChangeCurrencyButtonClickedListener)
        //set the change currency button clicked listener
        val selectSnapshotButton = toolbar.findViewById<ImageButton>(R.id.button_snapshots)
        selectSnapshotButton?.setOnClickListener(onSnapshotButtonClickedListener)
        Log.d("Cata", "have toolbar!")
    }

    private fun getListenerForDialogType(dialogType: BookmarksSelectionDialogType): OnItemSelectedListener {
        return when (dialogType) {
            BookmarksSelectionDialogType.ChangeCurrency ->
                { item -> bookmarksPresenter.displayCurrencyChanged(item) }
            BookmarksSelectionDialogType.SelectSnapshot ->
                { item -> bookmarksPresenter.selectedSnapshotChanged(item) }
        }
    }

    companion object {
        const val TAG = "BookmarksView"
    }
}