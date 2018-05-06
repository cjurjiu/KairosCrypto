package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerFragment
import com.example.cryptodrawablesprovider.getCryptoDrawable
import kotlinx.android.synthetic.main.layout_fragment_main_coin_details.view.*
import javax.inject.Inject

class CoinDetailsFragment :
        DaggerFragment<CoinDetailsComponent>(),
        NamedComponent,
        BackEventAwareComponent,
        CoinDetailsContract.CoinDetailsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter

    private var coinNameTextView: AppCompatTextView? = null
    private var coinSymbolTextView: AppCompatTextView? = null
    private var coinLogoImageView: AppCompatImageView? = null
    private var coinTrendImageView: AppCompatImageView? = null

    class Factory(private val activityComponent: ActivityComponent,
                  private val cryptoCoin: CryptoCoin)
        : DaggerFragmentFactory<CoinDetailsComponent>() {

        override fun onCreateFragment(): DaggerFragment<CoinDetailsComponent> {
            val f = CoinDetailsFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEY_COIN_ID, cryptoCoin.id)
            bundle.putString(ARG_KEY_COIN_NAME, cryptoCoin.name)
            bundle.putString(ARG_KEY_COIN_SYMBOL, cryptoCoin.symbol)
            bundle.putFloat(ARG_KEY_COIN_CHANGE_1H, cryptoCoin.percentChange1h)
            //set selected coin
            f.arguments = bundle
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateDaggerComponent(): CoinDetailsComponent {
            return activityComponent.getCoinDetailsComponent(CoinDetailsModule())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoCoinId = arguments!!.getString(ARG_KEY_COIN_ID)
        val cryptoCoinName = arguments!!.getString(ARG_KEY_COIN_NAME)
        val cryptoCoinSymbol = arguments!!.getString(ARG_KEY_COIN_SYMBOL)
        val cryptoCoinChange1h = arguments!!.getFloat(ARG_KEY_COIN_CHANGE_1H)
        Log.d("CataDetails", "Coin id is: $cryptoCoinId")
        injector.inject(this)
        coinDetailsPresenter.setInitialInfo(coinName = cryptoCoinName,
                coinId = cryptoCoinId,
                coinSymbol = cryptoCoinSymbol,
                change1h = cryptoCoinChange1h)
        Log.d(TAG, "CoinDetailsFragment${hashCode().toString(16)}#onCreate.injector:" +
                injector.hashCode().toString(16) + " presenter:" +
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
        coinNameTextView = view.text_coin_details_coin_name
        coinSymbolTextView = view.text_coin_details_coin_symbol
        coinLogoImageView = view.image_coin_details_coin_logo
        coinTrendImageView = view.image_coin_details_trend
        initializeToolbar(view)
        initializeViewPagerWithTabs(view)
    }

    override fun setCoinInfo(coinName: String, coinSymbol: String, change1h: Float) {
        coinNameTextView?.text = coinName
        coinSymbolTextView?.text = coinSymbol
        coinLogoImageView?.setImageDrawable(getCryptoDrawable(cryptoIdentifier = coinSymbol, context = activity!!))
        coinTrendImageView?.setImageResource(R.drawable.ic_vector_trending_up_black_24dp)
        //do just log for the moment
        Log.d(TAG, "$TAG#setCoinInfo.")
    }

    override fun showLoadingIndicator() {
        Log.d(TAG, "$TAG#showLoadingIndicator()")
    }

    override fun hideLoadingIndicator() {
        Log.d(TAG, "$TAG#hideLoadingIndicator()")
    }

    override fun onBack(): Boolean {
        Log.d("Cata", "CoinDetailsFragment back pressed")
        return false
    }

    override fun getPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return coinDetailsPresenter
    }

    private fun initializeToolbar(view: View) {
        val toolbar = view.toolbar_coin_details
        toolbar.title = ""
        (activity!! as AppCompatActivity).setSupportActionBar(toolbar)
        (activity!! as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeViewPagerWithTabs(view: View) {
        //ViewPager config
        val viewPager = view.view_pager_coin_details
        val coinDetailsViewPagerAdapter = CoinDetailsViewPagerAdapter(
                coinId = coinDetailsPresenter.getCoinId(),
                coinSymbol = coinDetailsPresenter.getCoinSymbol(),
                coinDetailsComponent = injector,
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
