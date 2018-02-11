package com.catalinj.smartpersist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinj.smartpersist.atomics.BackEventAwareComponent
import com.catalinj.smartpersist.atomics.Identifiable

/**
 * Created by catalin on 06.02.18.
 */
abstract class SmartPersistActivity<out DaggerComponent : Any> : AppCompatActivity(), Identifiable<String> {

    val fragmentNavigator: FragmentNavigator by lazy {
        FragmentNavigator(fragmentManager = supportFragmentManager)
    }

    private var lastConfigChangeObjPersisted: Any? = null
    private lateinit var myInjector: DaggerComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastConfigChangeObjPersisted = lastCustomNonConfigurationInstance
    }

    final override fun onRetainCustomNonConfigurationInstance(): Any {
        val retainedObjectsMap: MutableMap<String, Any> = mutableMapOf()
        retainedObjectsMap[getIdentity()] = myInjector
        if (retainsFragments()) {
            fragmentNavigator.listFragments()
                    .filterIsInstance<SmartPersistFragment<*>>()
                    .onEach {
                        if (!it.isInjectorAvailable()) {
                            it.forceInitInjector(this)
                        }
                    }
                    .map { it.getRetainable() }
                    .forEach { retainedObjectsMap.putAll(it) }
        }
        Log.d(getIdentity(), "MainActivity#onRetainCustomNonConfigurationInstance finishing: $isFinishing. my any: $retainedObjectsMap. keys count: ${retainedObjectsMap.size}")
        retainedObjectsMap.putAll(onRetainConfiguration())
        return retainedObjectsMap
    }

    override fun getLastCustomNonConfigurationInstance(): Any? {
        val lastConfigChangeObj = super.getLastCustomNonConfigurationInstance()
        return lastConfigChangeObj ?: lastConfigChangeObjPersisted
    }

    override fun onBackPressed() {
        var backConsumed: Boolean = fragmentNavigator.listFragments()
                .filterIsInstance<BackEventAwareComponent>()
                .any { it.onBack() }
        if (!backConsumed) {
            backConsumed = fragmentNavigator.popBackStackImmediate()
        }

        if (backConsumed) {
            return
        } else {
            super.onBackPressed()
        }
    }

    fun removedSavedInformation(identity: String) {
        val removed = (lastConfigChangeObjPersisted as MutableMap<String, Any>?)?.remove(identity)
        Log.d("Cata", "Remove for $identity result: $removed")
    }

    protected fun getInjector(): DaggerComponent {
        if (!::myInjector.isInitialized) {
            initInjector()
        }
        return myInjector
    }

    protected abstract fun createInjector(): DaggerComponent

    protected open fun onRetainConfiguration(): Map<String, Any> {
        return emptyMap()
    }

    protected open fun retainsFragments(): Boolean {
        return true
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

}