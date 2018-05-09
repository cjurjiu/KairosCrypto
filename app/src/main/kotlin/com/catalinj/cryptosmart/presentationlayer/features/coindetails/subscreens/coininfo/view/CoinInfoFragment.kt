package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.components.CoinInfoComponent
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinjurjiu.smartpersist.DaggerFragment
import kotlinx.android.synthetic.main.layout_fragment_coin_info.view.*
import javax.inject.Inject

/**
 * A simple view(fragment) which displays various information about a particular cryptocurrency.
 */
class CoinInfoFragment : DaggerFragment<CoinInfoComponent>(), CoinInfoContract.CoinInfoView {

    override val name: String = TAG

    @Inject
    protected lateinit var coinInfoPresenter: CoinInfoContract.CoinInfoPresenter

    private lateinit var coinName: TextView
    private lateinit var coinSymbol: TextView
    private lateinit var coinValue: TextView
    private lateinit var coinRank: TextView
    private lateinit var coinChange: TextView
    private lateinit var coinTimestamp: TextView

    class Factory(private val coinId: String,
                  private val coinSymbol: String,
                  private val coinDetailsComponent: CoinDetailsComponent)
        : DaggerFragmentFactory<CoinInfoComponent>() {

        override fun onCreateFragment(): DaggerFragment<CoinInfoComponent> {
            val fragment = CoinInfoFragment()
            val args = Bundle()
            args.putString(ARGS_COIN_ID, coinId)
            args.putString(ARGS_COIN_SYMBOL, coinSymbol)
            fragment.arguments = args
            return fragment
        }

        override fun onCreateDaggerComponent(): CoinInfoComponent {
            return coinDetailsComponent.getCoinInfoComponent(coinInfoModule = CoinInfoModule())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(coinInfoFragment = this)
        val coinId = arguments!!.getString(ARGS_COIN_ID)
        val coinSymbol = arguments!!.getString(ARGS_COIN_SYMBOL)
        coinInfoPresenter.setCoinId(coinId = coinId)
        coinInfoPresenter.setCoinSymbol(coinSymbol = coinSymbol)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_coin_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinInfoPresenter.viewAvailable(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coinInfoPresenter.viewDestroyed()
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

    override fun getPresenter(): CoinInfoContract.CoinInfoPresenter {
        return coinInfoPresenter
    }

    override fun setCoinInfo(coinDetails: CryptoCoinDetails) {
        coinName.text = coinDetails.name
        coinSymbol.text = coinDetails.symbol
        coinValue.text = coinDetails.priceData.price.toString()
        coinRank.text = coinDetails.rank.toString()
        coinChange.text = coinDetails.priceData.percentChange1h.toString()
        coinTimestamp.text = coinDetails.lastUpdated.toString()
    }

    override fun onStart() {
        super.onStart()
        coinInfoPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        coinInfoPresenter.stopPresenting()
    }

    companion object {
        const val TAG = "CoinInfoFragment"
        const val ARGS_COIN_ID = "ARGS::COIN_ID"
        const val ARGS_COIN_SYMBOL = "ARGS::COIN_SYMBOL"
    }
}