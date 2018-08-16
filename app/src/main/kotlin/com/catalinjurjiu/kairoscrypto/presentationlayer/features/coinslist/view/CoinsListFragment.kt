package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.di.components.ActivityComponent
import com.catalinjurjiu.kairoscrypto.di.components.CoinListComponent
import com.catalinjurjiu.kairoscrypto.di.modules.coinlist.CoinListModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.MainActivity
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.toMessageResId
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.CryptoListAdapterSettings
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_coin_list.view.*
import javax.inject.Inject


/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListFragment : WheelbarrowFragment<CoinListComponent>(),
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
    private lateinit var floatingScrollToTopButton: ScrollToTopFloatingActionButton
    private lateinit var optionsToolbar: CoinDisplayOptionsToolbar

    //android lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_coin_list, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinListPresenter.navigator = (activity as MainActivity).navigator
        (activity as MainActivity).showBottomNavigation()
        coinListPresenter.viewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
        coinListPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        coinListPresenter.stopPresenting()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.clearOnScrollListeners()
        coinListPresenter.viewDestroyed()
        //notify the toolbar that the view is destroyed
        optionsToolbar.getPresenter().viewDestroyed()
        //notify the floating scroll-to-top button that the view is destroyed
        floatingScrollToTopButton.getPresenter().viewDestroyed()
    }
    //end android lifecycle methods

    //mvp view methods
    override fun initialise() {
        val view = view!!
        val appCompatActivity = (activity as AppCompatActivity)
        initToolbar(view = view, appCompatActivity = appCompatActivity)
        initRecyclerView(view = view, appCompatActivity = appCompatActivity)
        initSwipeRefreshLayout(view = view)
        initFloatingActionButton(view = view)
    }

    override fun onBack(): Boolean {
        return false
    }

    override fun getPresenter(): CoinsListContract.CoinsListPresenter {
        return coinListPresenter
    }
    //end mvp view methods

    //coin list view methods
    override fun setListData(coinList: List<CryptoCoin>) {
        recyclerViewAdapter.coins = coinList
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

    override fun setContentVisible(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    //loading view methods
    override fun showLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }
    //end coin list & loading view methods

    private fun initToolbar(view: View, appCompatActivity: AppCompatActivity) {
        optionsToolbar = view.screen_toolbar
        appCompatActivity.setSupportActionBar(optionsToolbar)
        lifecycle.addObserver(optionsToolbar)
        //also inject the toolbar with the same injector
        cargo.inject(optionsToolbar)
        //notify the toolbar's presenter that its view is available
        optionsToolbar.getPresenter().viewAvailable(optionsToolbar)
    }

    private fun initSwipeRefreshLayout(view: View) {
        swipeRefreshLayout = view.swiperefreshlayout_coin_lists
        swipeRefreshLayout.setOnRefreshListener { coinListPresenter.userPullToRefresh() }
    }

    private fun initRecyclerView(view: View, appCompatActivity: AppCompatActivity) {
        val adapterSettings = CryptoListAdapterSettings(currency = coinListPresenter.displayCurrency,
                snapshot = coinListPresenter.displaySnapshot)

        recyclerView = view.recyclerview_coins_list
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
     * Factory for this Fragment
     */
    class Factory(private val activityComponent: ActivityComponent) : WheelbarrowFragment.Factory<CoinListComponent>() {

        override fun onCreateFragment(): WheelbarrowFragment<CoinListComponent> {
            val f = CoinsListFragment()
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateCargo(): CoinListComponent {
            return activityComponent.getCoinListComponent(CoinListModule())
        }
    }

    companion object {
        const val TAG = "CoinsListFragment"
        private const val SCROLL_TO_TOP_LIST_THRESHOLD = 30
    }
}