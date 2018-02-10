package com.catalinj.cryptosmart.common.cryptobase

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinj.cryptosmart.MainActivity
import com.catalinj.cryptosmart.common.atomics.BackEventAwareComponent
import com.catalinj.cryptosmart.common.atomics.Identifiable

/**
 * Created by catalin on 06.02.18.
 */
abstract class BaseFragment<out DaggerComponent : Any> : Fragment(), Identifiable<String>, BackEventAwareComponent {

    private lateinit var myInjector: DaggerComponent

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initInjector((context as AppCompatActivity?)!!)
        Log.d("BaseFragment", "BaseFragment#onAttach end. my CoinListComponent: ${myInjector.hashCode()}")
    }

    @Suppress("UNCHECKED_CAST")
    private fun initInjector(activity: AppCompatActivity) {
        val stateMap: Map<String, Any>? = activity.lastCustomNonConfigurationInstance as Map<String, Any>?
        myInjector = stateMap?.get(getIdentity()) as DaggerComponent? ?: createInjector(activity)
    }

    protected fun getInjector(): DaggerComponent {
        return myInjector
    }

    fun isInjectorAvailable(): Boolean {
        return ::myInjector.isInitialized
    }

    fun forceInitInjector(activity: AppCompatActivity) {
        initInjector(activity)
    }

    abstract fun createInjector(activity: AppCompatActivity): DaggerComponent

    abstract override fun onBack(): Boolean

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving && !activity.isChangingConfigurations) {
            (activity as MainActivity).removedSavedInformation(getIdentity())
        }
    }
}