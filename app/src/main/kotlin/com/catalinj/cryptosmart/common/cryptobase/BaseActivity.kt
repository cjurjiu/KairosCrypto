package com.catalinj.cryptosmart.common.cryptobase;

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinj.cryptosmart.common.atomics.BackEventAwareComponent
import com.catalinj.cryptosmart.common.atomics.HasRetainable
import com.catalinj.cryptosmart.common.atomics.Identifiable

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
            supportFragmentManager.fragments
                    .filterIsInstance<HasRetainable<Map<String, Any>>>()
                    .map { it.getRetainable() }
                    .forEach { retainedObjectsMap.putAll(it) }
        }
        Log.d(getIdentity(), "MainActivity#onRetainCustomNonConfigurationInstance finishing: $isFinishing. my any: $retainedObjectsMap. keys count: ${retainedObjectsMap.size}")
        retainedObjectsMap.putAll(onRetainConfiguration())
        return retainedObjectsMap
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

    override fun onBackPressed() {
        var backConsumed: Boolean = supportFragmentManager.fragments
                .filterIsInstance<BackEventAwareComponent>()
                .any { it.onBack() }
        if (!backConsumed) {
            backConsumed = supportFragmentManager.popBackStackImmediate()
        }
        if (backConsumed) {
            return
        } else {
            super.onBackPressed()
        }
    }
}
