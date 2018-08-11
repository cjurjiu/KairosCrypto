package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.di.components.ActivityComponent
import com.catalinjurjiu.kairoscrypto.di.components.CoinDetailsComponent
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.CoinDetailsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.MainActivity
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.trendlineForPercent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_main_coin_details.view.*
import javax.inject.Inject

class CoinDetailsFragment :
        WheelbarrowFragment<CoinDetailsComponent>(),
        NamedComponent,
        BackEventAwareComponent,
        CoinDetailsContract.CoinDetailsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter
    @Inject
    protected lateinit var imageHelper: ImageHelper<String>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var coinDetailsViewPagerAdapter: CoinDetailsViewPagerAdapter
    private lateinit var coinNameTextView: AppCompatTextView
    private lateinit var coinSymbolTextView: AppCompatTextView
    private lateinit var coinLogoImageView: AppCompatImageView
    private lateinit var coinTrendImageView: AppCompatImageView
    private lateinit var bookmarkToggleButton: ToggleButton

    class Factory(private val activityComponent: ActivityComponent,
                  private val cryptoCoin: CryptoCoin)
        : WheelbarrowFragment.Factory<CoinDetailsComponent>() {

        override fun onCreateFragment(): WheelbarrowFragment<CoinDetailsComponent> {
            return CoinDetailsFragment()
        }

        override fun onCreateCargo(): CoinDetailsComponent {
            return activityComponent.getCoinDetailsComponent(
                    coinListModule = CoinDetailsModule(CoinDetailsPartialData(coinName = cryptoCoin.name,
                            webFriendlyName = cryptoCoin.websiteSlug,
                            coinSymbol = cryptoCoin.symbol,
                            coinId = cryptoCoin.id,
                            change1h = cryptoCoin.priceData.entries.first().value.percentChange1h)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(this)
        Log.d(TAG, "CoinDetailsFragment${hashCode().toString(16)}#onCreate.injector:" +
                cargo.hashCode().toString(16) + " presenter:" +
                coinDetailsPresenter.hashCode().toString(16))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinDetailsFragment#onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_main_coin_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CoinDetailsFragment#onViewCreated")
        (activity as MainActivity).hideBottomNavigation()
        coinDetailsPresenter.navigator = (activity as MainActivity).navigator
        coinDetailsPresenter.viewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "CoinDetailsFragment#onStart")
        coinDetailsPresenter.startPresenting()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CoinDetailsFragment#onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CoinDetailsFragment#onStop")
        coinDetailsPresenter.stopPresenting()
        Log.d(TAG, "CoinDetailsFragment#onStop. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CoinDetailsFragment#onStop")
        Log.d(TAG, "CoinDetailsFragment#onStop. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "CoinDetailsFragment#onSaveInstanceState. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CoinDetailsFragment#onDestroyView")
        coinDetailsPresenter.viewDestroyed()
        Log.d(TAG, "CoinDetailsFragment#onDestroyView. isRemoving:$isRemoving " +
                "isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinDetailsFragment#onDestroy")
        Log.d(TAG, "CoinDetailsFragment#onDestroy. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinDetailsFragment#onDetach")
    }

    override fun initialise() {
        val view = view!!
        swipeRefreshLayout = view.coin_details_swipe_refresh_layout
        swipeRefreshLayout.setOnRefreshListener {
            coinDetailsPresenter.userPullToRefresh()
        }
        coinNameTextView = view.text_coin_details_coin_name
        coinSymbolTextView = view.text_coin_details_coin_symbol
        coinLogoImageView = view.image_coin_details_coin_logo
        coinTrendImageView = view.image_coin_details_trend
        bookmarkToggleButton = view.button_coin_details_bookmarks_toggle
        initializeToolbar(view)
        initializeViewPagerWithTabs(view)
    }

    override fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float) {
        coinNameTextView.text = coinName
        coinSymbolTextView.text = coinSymbol
        imageHelper.setImage(imageView = coinLogoImageView, resourceIdentifier = coinSymbol)
        coinTrendImageView.trendlineForPercent(change1h)
        //do just log for the moment
        Log.d(TAG, "$TAG#setCoinInfo.")
    }

    override fun showLoadingIndicator() {
        Log.d(TAG, "$TAG#showLoadingIndicator()")
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        Log.d(TAG, "$TAG#hideLoadingIndicator()")
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onBack(): Boolean {
        Log.d("Cata", "CoinDetailsFragment back pressed")
        return false
    }

    override fun getPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return coinDetailsPresenter
    }

    override fun getActiveChildView(): MvpView<*, *> {
        val viewPager = view?.view_pager_coin_details
        val activeFragment = coinDetailsViewPagerAdapter.getItem(viewPager!!.currentItem)
        return (activeFragment as MvpView<*, *>)
    }

    override fun refreshBookmarkToggleButton(isBookmark: Boolean) {
        bookmarkToggleButton.setOnCheckedChangeListener(null)
        bookmarkToggleButton.isChecked = isBookmark
        bookmarkToggleButton.setOnCheckedChangeListener { _, isChecked ->
            coinDetailsPresenter.toggleButtonPressed(isChecked = isChecked)
        }
        bookmarkToggleButton.visibility = View.VISIBLE
    }

    private fun initializeToolbar(view: View) {
        val toolbar = view.toolbar_coin_details
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_vector_arrow_back_black_24dp, null)
        toolbar.title = ""
        (activity!! as AppCompatActivity).setSupportActionBar(toolbar)
        (activity!! as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeViewPagerWithTabs(view: View) {
        //ViewPager config
        val viewPager = view.view_pager_coin_details
        coinDetailsViewPagerAdapter = CoinDetailsViewPagerAdapter(
                coinDetailsPartialData = coinDetailsPresenter.getCoinData(),
                coinDetailsComponent = cargo,
                supportFragmentManager = childFragmentManager)
        viewPager.adapter = coinDetailsViewPagerAdapter
        //TabLayout config & setup with ViewPager
        val tabLayout = view.tab_layout_coin_details
        tabLayout.setupWithViewPager(viewPager)
    }

    internal companion object {
        const val TAG: String = "CoinDetailsFragment"
        private const val ARG_KEY_COIN_NAME = "ARG::COIN_NAME"
        private const val ARG_KEY_COIN_SYMBOL = "ARG::COIN_SYMBOL"
        private const val ARG_KEY_COIN_ID = "ARG::COIN_ID"
        private const val ARG_KEY_COIN_CHANGE_1H = "ARG::COIN_CHANGE_1H"
    }
}
