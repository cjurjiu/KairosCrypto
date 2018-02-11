package com.catalinj.smartpersist

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import com.catalinj.smartpersist.atomics.BackEventAwareComponent
import com.catalinj.smartpersist.atomics.HasRetainable
import com.catalinj.smartpersist.atomics.Identifiable


/**
 * Created by catalin on 06.02.18.
 */
abstract class SmartPersistFragment<out DaggerComponent : Any> : Fragment(),
        HasRetainable<Map<String, Any>>,
        Identifiable<String>,
        BackEventAwareComponent {

    private lateinit var myInjector: DaggerComponent
    protected lateinit var fragmentNavigator: FragmentNavigator

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context !is SmartPersistActivity<*>) {
            throw IllegalStateException("The SmartPersistFragment can only be attached toa SmartPersistActivity!")
        }

        val smartPersistActivity = context as SmartPersistActivity<*>
        this.fragmentNavigator = smartPersistActivity.fragmentNavigator
        initInjector(smartPersistActivity)
        Log.d("SmartPersistFragment", "SmartPersistFragment#onAttach end. my CoinListComponent: ${myInjector.hashCode()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving && !activity.isChangingConfigurations) {
            (activity as SmartPersistActivity<*>).removedSavedInformation(getIdentity())
        }
    }

    override fun onDetach() {
        super.onDetach()
        //todo, set navigator to  null?
    }

    abstract override fun onBack(): Boolean

    protected fun getInjector(): DaggerComponent {
        return myInjector
    }

    fun isInjectorAvailable(): Boolean {
        return ::myInjector.isInitialized
    }

    fun forceInitInjector(activity: SmartPersistActivity<*>) {
        initInjector(activity)
    }

    abstract fun createInjector(activity: SmartPersistActivity<*>): DaggerComponent

    @Suppress("UNCHECKED_CAST")
    private fun initInjector(activity: SmartPersistActivity<*>) {
        val stateMap: Map<String, Any>? = activity.lastCustomNonConfigurationInstance as Map<String, Any>?
        myInjector = stateMap?.get(getIdentity()) as DaggerComponent? ?: createInjector(activity)
    }
}