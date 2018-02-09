package com.catalinj.cryptosmart.features.coindetails.view


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.MainActivity
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.common.atomics.HasRetainable
import com.catalinj.cryptosmart.common.cryptobase.BaseFragment
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.features.coinslist.contract.CoinDetailsContract
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment
import com.catalinj.cryptosmart.network.CoinMarketCapCryptoCoin
import com.google.android.gms.plus.PlusOneButton
import javax.inject.Inject


/**
 * A fragment with a Google +1 button.
 */
class CoinDetailsFragment : BaseFragment<CoinDetailsComponent>(), HasRetainable<Map<String, Any>>,
        CoinDetailsContract.CoinDetailsView {

    // The URL to +1.  Must be a valid URL.
    private val PLUS_ONE_URL = "http://developer.android.com"
    private var mPlusOneButton: PlusOneButton? = null
    @Inject
    protected lateinit var coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getInjector().inject(this)

        Log.d(CoinsListFragment.TAG, "CoinsListFragment#onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_plus_one, container, false)

        //Find the +1 button
        mPlusOneButton = view.findViewById<View>(R.id.plus_one_button) as PlusOneButton

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinDetailsPresenter.onViewAvailable(this)
    }

    override fun onResume() {
        super.onResume()
        getPresenter().receivedFocus()
    }

    override fun getIdentity(): String {
        return TAG
    }

    override fun createInjector(): CoinDetailsComponent {
        return (activity as MainActivity).getCoinDetailsComponent()
    }

    override fun getPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return coinDetailsPresenter
    }

    override fun getRetainable(): Map<String, Any> {
        val retainablesMap = mutableMapOf<String, Any>()
        retainablesMap[CoinsListFragment.TAG] = getInjector()
        return retainablesMap
    }

    override fun setCoinData(data: CoinMarketCapCryptoCoin) {
        //todo
    }

    override fun showLoadingIndicator() {
        //todo
    }

    override fun hideLoadingIndicator() {
        //todo
    }

    override fun onBack(): Boolean {
        return false
    }

    override fun increaseValue() {
        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton!!.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE)
    }

    internal companion object {
        const val TAG: String = "CoinDetailsComponent"
        // The request code must be 0 or greater.
        private const val PLUS_ONE_REQUEST_CODE = 0
    }

}
