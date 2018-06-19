package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.components.CoinMarketsComponent
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinjurjiu.wheelbarrow.InjectorFragment
import kotlinx.android.synthetic.main.fragment_coin_markets.view.*
import javax.inject.Inject

/**
 * A simple view(fragment) which displays the exchanges on which a particular crypocurrency is
 * available for trade.
 */
class CoinMarketsFragment : InjectorFragment<CoinMarketsComponent>(), CoinMarketsContract.CoinMarketsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: MarketsInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_markets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinMarketsPresenter.viewAvailable(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coinMarketsPresenter.viewDestroyed()
    }

    override fun initialise() {
        recyclerView = view!!.recyclerview_markets_list
        recyclerViewAdapter = MarketsInfoAdapter(emptyList())
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        coinMarketsPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        coinMarketsPresenter.stopPresenting()
    }

    override fun getPresenter(): CoinMarketsContract.CoinMarketsPresenter {
        return coinMarketsPresenter
    }

    override fun setData(marketInfo: List<CryptoCoinMarketInfo>) {
        recyclerViewAdapter.data = marketInfo
        recyclerViewAdapter.notifyDataSetChanged()
    }

    class Factory(private val coinData: CoinDetailsPartialData,
                  private val coinDetailsComponent: CoinDetailsComponent)

        : InjectorFragmentFactory<CoinMarketsComponent>() {
        override fun onCreateFragment(): InjectorFragment<CoinMarketsComponent> {
            return CoinMarketsFragment()
        }

        override fun onCreateInjector(): CoinMarketsComponent {
            return coinDetailsComponent.getCoinMarketsComponent(
                    coinMarketsModule = CoinMarketsModule(coinData)
            )
        }
    }

    companion object {
        const val TAG = "CoinMarketsFragment"
    }

}