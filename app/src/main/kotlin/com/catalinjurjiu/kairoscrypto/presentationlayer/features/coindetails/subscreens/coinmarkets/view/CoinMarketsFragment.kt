package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
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
    private lateinit var floatingScrollToTopButton: View
    private var hideAnimationInProgress: Boolean = false

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
    }

    override fun initialise() {
        recyclerView = view!!.recyclerview_markets_list
        recyclerViewAdapter = MarketsInfoAdapter(emptyList())
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                coinMarketsPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }
        })
        floatingScrollToTopButton = view!!.button_floating_scroll_to_top
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

    //scroll to top behavior
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
                    coinMarketsPresenter.scrollToTopPressed()
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
    }

}