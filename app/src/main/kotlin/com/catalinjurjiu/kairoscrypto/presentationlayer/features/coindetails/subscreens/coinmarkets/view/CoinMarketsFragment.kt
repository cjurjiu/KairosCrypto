package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.di.components.CoinDetailsComponent
import com.catalinjurjiu.kairoscrypto.di.components.CoinMarketsComponent
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.toMessageResId
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import kotlinx.android.synthetic.main.fragment_coin_markets.view.*
import javax.inject.Inject

/**
 * A simple view(fragment) which displays the exchanges on which a particular crypocurrency is
 * available for trade.
 */
class CoinMarketsFragment : WheelbarrowFragment<CoinMarketsComponent>(), CoinMarketsContract.CoinMarketsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: MarketsInfoAdapter
    private lateinit var floatingScrollToTopButton: ScrollToTopFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(this)
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
        recyclerView.clearOnScrollListeners()
        floatingScrollToTopButton.getPresenter().viewDestroyed()
    }

    override fun initialise() {
        recyclerView = view!!.recyclerview_markets_list
        recyclerViewAdapter = MarketsInfoAdapter(emptyList())
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        initFloatingActionButton(view = view!!)
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

    override fun showError(errorCode: ErrorCode, retryAction: () -> Unit) {
        SnackBarWrapper.showSnackBarWithAction(view = view!!,
                infoMessageRes = errorCode.toMessageResId(),
                actionMessageRes = R.string.cta_try_again,
                clickListener = View.OnClickListener {
                    //invoke the retry action
                    retryAction.invoke()
                })
    }

    class Factory(private val coinData: CoinDetailsPartialData,
                  private val coinDetailsComponent: CoinDetailsComponent)

        : WheelbarrowFragment.Factory<CoinMarketsComponent>() {
        override fun onCreateFragment(): WheelbarrowFragment<CoinMarketsComponent> {
            return CoinMarketsFragment()
        }

        override fun onCreateCargo(): CoinMarketsComponent {
            return coinDetailsComponent.getCoinMarketsComponent(
                    coinMarketsModule = CoinMarketsModule(coinData)
            )
        }
    }

    companion object {
        const val TAG = "CoinMarketsFragment"
        private const val SCROLL_TO_TOP_LIST_THRESHOLD = 20
    }

}