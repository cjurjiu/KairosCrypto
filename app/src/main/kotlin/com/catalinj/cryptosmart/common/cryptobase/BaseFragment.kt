package com.catalinj.cryptosmart.common.cryptobase

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.catalinj.cryptosmart.common.atomics.Identifiable
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment

/**
 * Created by catalin on 06.02.18.
 */
abstract class  BaseFragment<out DaggerComponent : Any> : Fragment(), Identifiable<String> {

    private lateinit var myInjector: DaggerComponent

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context!! as FragmentActivity
        val stateMap: Map<String, Any>? = activity.lastCustomNonConfigurationInstance as Map<String, Any>?
        myInjector = stateMap?.get(getIdentity()) as DaggerComponent? ?: createInjector()
        Log.d(CoinsListFragment.TAG, "CoinsListFragment#onAttach end. my CoinListComponent: $myInjector")
    }

    protected fun getInjector(): DaggerComponent {
        return myInjector
    }

    abstract fun createInjector(): DaggerComponent
}