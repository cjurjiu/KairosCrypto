package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.common.extension.toMessageResId
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.common.view.CryptoListAdapterSettings
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.InjectorFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_coin_list.view.*
import javax.inject.Inject


/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListFragment :
        InjectorFragment<CoinListComponent>(),
        NamedComponent,
        BackEventAwareComponent,
        CoinsListContract.CoinsListView {

    override val name: String = TAG

    @Inject
    protected lateinit var coinListPresenter: CoinsListContract.CoinsListPresenter

    @Inject
    protected lateinit var imageHelper: ImageHelper<String>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerViewAdapter: CoinListAdapter

    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    private lateinit var floatingScrollToTopButton: View

    private lateinit var optionsToolbar: CoinDisplayOptionsToolbar

    private var hideAnimationInProgress: Boolean = false

    //android lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        Log.d(TAG, "CoinsListFragment${hashCode().toString(16)}#onCreate.injector:" + injector.hashCode().toString(16) + " presenter:" + coinListPresenter.hashCode().toString(16))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinsListFragment#onCreateView")
        return inflater.inflate(R.layout.layout_fragment_coin_list, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinListPresenter.navigator = (activity as MainActivity).navigator
        (activity as MainActivity).showBottomNavigation()
        Log.d(TAG, "CoinsListFragment#onViewCreated")
        coinListPresenter.viewAvailable(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CoinsListFragment#onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CoinsListFragment#onPause")
    }

    override fun onStart() {
        super.onStart()
        coinListPresenter.startPresenting()
        Log.d(TAG, "CoinsListFragment#onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CoinsListFragment#onStop")
        coinListPresenter.stopPresenting()
        Log.d(TAG, "CoinsListFragment#onStop. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "CoinsListFragment#onSaveInstanceState. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CoinsListFragment#onDestroyView")
        recyclerView.clearOnScrollListeners()
        coinListPresenter.viewDestroyed()
        //also notify the toolbar that the view is destroyed
        optionsToolbar.getPresenter().viewDestroyed()
        Log.d(TAG, "CoinsListFragment#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinsListFragment#onDestroy")
        Log.d(TAG, "CoinsListFragment#onDestroy. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinsListFragment#onDetach")
    }
    //end android lifecycle methods

    //mvp view methods
    override fun initialise() {
        val view = view!!
        val appCompatActivity = (activity as AppCompatActivity)
        initToolbar(rootView = view, appCompatActivity = appCompatActivity)
        initRecyclerView(rootView = view, appCompatActivity = appCompatActivity)
        initSwipeRefreshLayout(rootView = view)
        this.floatingScrollToTopButton = view.button_floating_scroll_to_top
    }

    override fun onBack(): Boolean {
        Log.d(TAG, "CoinsListFragment#onBack")
        return false
    }

    override fun getPresenter(): CoinsListContract.CoinsListPresenter {
        return coinListPresenter
    }
    //end mvp view methods

    //coin list view methods
    override fun setListData(data: List<CryptoCoin>) {
        recyclerViewAdapter.coins = data
        recyclerViewAdapter.adapterSettings = CryptoListAdapterSettings(currency = coinListPresenter.displayCurrency,
                snapshot = coinListPresenter.displaySnapshot)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun refreshContent() {
        recyclerViewAdapter.adapterSettings = CryptoListAdapterSettings(currency = coinListPresenter.displayCurrency,
                snapshot = coinListPresenter.displaySnapshot)
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
                    coinListPresenter.scrollToTopPressed()
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
        Log.d("Cata", "Loading started")
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        Log.d("Cata", "Loading stopped")
        swipeRefreshLayout.isRefreshing = false
    }
    //end coin list & loading view methods

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

    private fun initSwipeRefreshLayout(rootView: View) {
        swipeRefreshLayout = rootView.swiperefreshlayout_coin_lists
        swipeRefreshLayout.setOnRefreshListener { coinListPresenter.userPullToRefresh() }
    }

    private fun initRecyclerView(rootView: View, appCompatActivity: AppCompatActivity) {
        val adapterSettings = CryptoListAdapterSettings(currency = coinListPresenter.displayCurrency,
                snapshot = coinListPresenter.displaySnapshot)

        recyclerView = rootView.recyclerview_coins_list
        recyclerViewAdapter = CoinListAdapter(context = appCompatActivity.baseContext,
                coins = emptyList(),
                adapterSettings = adapterSettings,
                imageHelper = imageHelper) { coinListPresenter.coinSelected(it) }
        recyclerViewLayoutManager = LinearLayoutManager(appCompatActivity.baseContext)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = recyclerViewLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                coinListPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }
        })
    }

    /**
     * Factory for this Fragment
     */
    class Factory(private val activityComponent: ActivityComponent) : InjectorFragmentFactory<CoinListComponent>() {

        override fun onCreateFragment(): InjectorFragment<CoinListComponent> {
            val f = CoinsListFragment()
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateInjector(): CoinListComponent {
            return activityComponent.getCoinListComponent(CoinListModule())
        }
    }

    companion object {
        const val TAG = "CoinsListFragment"
    }
}