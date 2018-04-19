package com.catalinj.cryptosmart.features.coinslist.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.MainActivity
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.features.coindetails.view.CoinDetailsFragment
import com.catalinj.cryptosmart.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerFragment
import kotlinx.android.synthetic.main.layout_fragment_coin_list.view.*
import javax.inject.Inject

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListFragment : DaggerFragment<CoinListComponent>(), NamedComponent, BackEventAwareComponent,
        CoinsListContract.CoinsListView {

    override val name: String = TAG

    @Inject
    protected lateinit var coinListPresenter: CoinsListContract.CoinsListPresenter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerViewAdapter: CoinListAdapter

    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    class Factory(private val activityComponent: ActivityComponent) : DaggerFragmentFactory<CoinListComponent>() {

        override fun onCreateFragment(): DaggerFragment<CoinListComponent> {
            val f = CoinsListFragment()
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateDaggerComponent(): CoinListComponent {
            return activityComponent.getCoinListComponent(CoinListModule())
        }
    }

    //view methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        coinListPresenter.startPresenting()
        Log.d(TAG, "CoinsListFragment${hashCode().toString(16)}#onCreate.injector:" + injector.hashCode().toString(16) + " presenter:" + coinListPresenter.hashCode().toString(16))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinsListFragment#onCreateView")
        val v: View = inflater.inflate(R.layout.layout_fragment_coin_list, container, false)!!
        recyclerView = v.recyclerview_coins_list!!
        recyclerViewAdapter = CoinListAdapter(activity!!.baseContext, emptyList()) {
            val activityComponent = (activity as MainActivity).injector
            val fragmentFactory = CoinDetailsFragment.Factory(activityComponent)
            val frag = fragmentFactory.create()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container, frag, CoinDetailsFragment.TAG)
                    .addToBackStack(CoinDetailsFragment.TAG)
                    .commit()
        }
        recyclerViewLayoutManager = LinearLayoutManager(activity!!.baseContext)

        swipeRefreshLayout = v.swiperefreshlayout_coin_lists
        swipeRefreshLayout.setOnRefreshListener { coinListPresenter.userPullToRefresh() }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = recyclerViewLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                coinListPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CoinsListFragment#onViewCreated")
        coinListPresenter.onViewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
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
        coinListPresenter.onViewDestroyed()
        Log.d(TAG, "CoinsListFragment#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinsListFragment#onDestroy")
        //TODO release presenter reference?
        Log.d(TAG, "CoinsListFragment#onDestroy. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinsListFragment#onDetach")
    }

    override fun onBack(): Boolean {
        Log.d(TAG, "CoinsListFragment#onBack")
        return false
    }

    override fun getPresenter(): CoinsListContract.CoinsListPresenter {
        return coinListPresenter
    }

    //coin list view presenter
    override fun setListData(data: List<CoinMarketCapCryptoCoin>) {
        (recyclerView.adapter as CoinListAdapter).coins = data
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun showLoadingIndicator() {
        Log.d("Cata", "Loading started")
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        Log.d("Cata", "Loading stopped")
        swipeRefreshLayout.isRefreshing = false
    }

    override fun scrollTo(scrollPosition: Int) {
        recyclerView.scrollToPosition(scrollPosition)
    }

    companion object {
        const val TAG = "CoinsListFragment"
    }
}