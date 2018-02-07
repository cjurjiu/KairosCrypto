package com.catalinj.cryptosmart.common;

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinj.cryptosmart.features.HasRetainable

/**
 * Created by catalin on 06.02.18.
 */
abstract class BaseActivity<out DaggerComponent : Any> : AppCompatActivity(), Identifiable<String> {
    private lateinit var myInjector: DaggerComponent

    protected fun getInjector(): DaggerComponent {
        if (!::myInjector.isInitialized) {
            initInjector()
        }
        return myInjector
    }

    @Suppress("UNCHECKED_CAST")
    private fun initInjector() {
        val retainedMap: Map<String, Any>? = lastCustomNonConfigurationInstance as Map<String, Any>?
        myInjector = if (retainedMap != null && retainedMap.containsKey(getIdentity())) {
            Log.d(getIdentity(), "initComponent: use lastNonConfigurationInstance.")
            retainedMap[getIdentity()] as DaggerComponent
        } else {
            Log.d(getIdentity(), "initComponent: create new from application.")
            createInjector()
        }
    }

    protected open fun retainsFragments(): Boolean {
        return false
    }

    protected open fun onRetainConfiguration(): Map<String, Any> {
        return emptyMap()
    }

    protected abstract fun createInjector(): DaggerComponent

    final override fun onRetainCustomNonConfigurationInstance(): Any {
        val retainedObjectsMap: MutableMap<String, Any> = mutableMapOf()
        retainedObjectsMap[getIdentity()] = myInjector
        if (retainsFragments()) {
            supportFragmentManager.fragments.filterIsInstance<HasRetainable<Pair<String, Any>>>()
                    .map { fragment -> fragment.getRetainable() }
                    .forEach { pair -> retainedObjectsMap[pair.first] = pair.second }
        }
        Log.d(getIdentity(), "MainActivity#onRetainCustomNonConfigurationInstance finishing: $isFinishing. my any: $retainedObjectsMap. keys count: ${retainedObjectsMap.size}")
        retainedObjectsMap.putAll(onRetainConfiguration())
        return retainedObjectsMap
    }


}
