package com.catalinj.cryptosmart.presentationlayer.features.coindetails.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.contract.CoinDetailsContract
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerFragment
import kotlinx.android.synthetic.main.layout_fragment_coin_details.view.*
import javax.inject.Inject

class CoinDetailsFragment :
        DaggerFragment<CoinDetailsComponent>(),
        NamedComponent,
        BackEventAwareComponent,
        CoinDetailsContract.CoinDetailsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter
    private lateinit var coinName: TextView
    private lateinit var coinSymbol: TextView
    private lateinit var coinValue: TextView
    private lateinit var coinRank: TextView
    private lateinit var coinChange: TextView
    private lateinit var coinTimestamp: TextView

    class Factory(private val activityComponent: ActivityComponent,
                  private val cryptoCoinId: String)
        : DaggerFragmentFactory<CoinDetailsComponent>() {

        override fun onCreateFragment(): DaggerFragment<CoinDetailsComponent> {
            val f = CoinDetailsFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEY_COIN_ID, cryptoCoinId)
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
        Log.d("CataDetails", "Coin id is: $cryptoCoinId")
        injector.inject(this)
        coinDetailsPresenter.setCoinId(cryptoCoinId)
        Log.d(TAG, "CoinDetailsFragment${hashCode().toString(16)}#onCreate.injector:" + injector.hashCode().toString(16) + " presenter:" + coinDetailsPresenter.hashCode().toString(16))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinDetailsFragment#onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_coin_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CoinDetailsFragment#onViewCreated")
        coinDetailsPresenter.viewAvailable(this)
    }

    override fun initialise() {
        val view = view!!
        coinName = view.coin_name
        coinSymbol = view.coin_symbol
        coinValue = view.coin_value
        coinRank = view.coin_rank
        coinChange = view.coin_change
        coinTimestamp = view.coin_timestamp
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
        Log.d(TAG, "CoinDetailsFragment#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
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

    override fun onBack(): Boolean {
        Log.d("Cata", "CoinDetailsFragment back pressed")
        return false
    }

    override fun getPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return coinDetailsPresenter
    }

    override fun setCoinData(coinDetails: CryptoCoinDetails) {
        coinName.text = coinDetails.name
        coinSymbol.text = coinDetails.symbol
        coinValue.text = coinDetails.priceUsd.toString()
        coinRank.text = coinDetails.rank.toString()
        coinChange.text = coinDetails.percentChange1h.toString()
        coinTimestamp.text = coinDetails.lastUpdated.toString()
    }

    override fun showLoadingIndicator() {
        Log.d(TAG, "$TAG#showLoadingIndicator()")
    }

    override fun hideLoadingIndicator() {
        Log.d(TAG, "$TAG#hideLoadingIndicator()")
    }

    internal companion object {
        const val TAG: String = "CoinDetailsFragment"
        private const val ARG_KEY_COIN_ID = "ARG::COIN_ID"
    }
}
