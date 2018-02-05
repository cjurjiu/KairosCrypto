package com.catalinj.cryptosmart.features.coinslist.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.catalinj.cryptosmart.DependencyRoot
import com.catalinj.cryptosmart.MainActivity
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.network.CoinMarketCapCryptoCoin
import kotlinx.android.synthetic.main.layout_fragment_coin_list.view.*
import javax.inject.Inject

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListFragment : Fragment(), CoinsListContract.CoinsListView {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: CoinListAdapter
    @Inject
    protected lateinit var coinListPresenter: CoinsListContract.CoinsListPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(TAG, "CoinsListFragment#onAttach")
        ((context!!.applicationContext) as DependencyRoot).getCoinListComponent().inject(this)
        val o = (context as MainActivity).lastCustomNonConfigurationInstance
        Log.d(TAG, "CoinsListFragment#onAttach end. my Any: $o")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val o = (activity as MainActivity).lastCustomNonConfigurationInstance
        Log.d(TAG, "CoinsListFragment#onCreate. my Any: $o")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinsListFragment#onCreateView")
        val v: View = inflater?.inflate(R.layout.layout_fragment_coin_list, container, false)!!
        recyclerView = v.recyclerview_coins_list!!
        recyclerViewAdapter = CoinListAdapter(activity.baseContext, emptyList())
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity.baseContext)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CoinsListFragment#onViewCreated")
        coinListPresenter.onViewAvailable(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "CoinsListFragment#onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "CoinsListFragment#onStart")
        coinListPresenter.startPresenting()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CoinsListFragment#onResume")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "CoinsListFragment#onActivityCreated")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CoinsListFragment#onPause")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "CoinsListFragment#onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CoinsListFragment#onStop")
        coinListPresenter.stopPresenting()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CoinsListFragment#onDestroyView")
        coinListPresenter.onViewDestroyed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinsListFragment#onDestroy")
        //TODO release presenter reference?
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinsListFragment#onDetach")
    }

    override fun getPresenter(): CoinsListContract.CoinsListPresenter {
        return coinListPresenter
    }

    override fun setListData(data: List<CoinMarketCapCryptoCoin>) {
        (recyclerView.adapter as CoinListAdapter).coins = data
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun showLoadingIndicator() {
        Toast.makeText(activity.baseContext, "Loading started", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoadingIndicator() {
        Toast.makeText(activity.baseContext, "Loading stopped", Toast.LENGTH_SHORT).show()
    }

    private companion object {
        const val TAG = "Cata"
    }
}