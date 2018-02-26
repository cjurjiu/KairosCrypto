package com.catalinj.smartpersist

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import com.catalinj.smartpersist.functional.BackEventAwareComponent
import com.catalinj.smartpersist.markes.HasRetainable
import com.catalinj.smartpersist.markes.NamedComponent


/**
 * Created by catalin on 06.02.18.
 */
abstract class SmartPersistFragment<out DaggerComponent : Any> : Fragment(),
        HasRetainable<Map<String, Any>>,
        NamedComponent,
        BackEventAwareComponent {

    override val retainable: Map<String, Any>
        get() {
            return computeRetainable()
        }

    protected lateinit var fragmentNavigator: FragmentNavigator
    protected lateinit var childFragmentNavigator: FragmentNavigator
    protected lateinit var savedRetainable: Map<String, Any>
    protected var smartPersistActivity: SmartPersistActivity<*>? = null
    private lateinit var myInjector: DaggerComponent

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context !is SmartPersistActivity<*>) {
            throw IllegalStateException("The SmartPersistFragment can only be attached toa SmartPersistActivity!")
        }

        performFragmentInit(context)

        this.fragmentNavigator = this.smartPersistActivity!!.fragmentNavigator
        this.childFragmentNavigator = FragmentNavigator(this.childFragmentManager,
                getChildFragNavigatorRetainable(retainable = savedRetainable))

        Log.d("SmartPersistFragment", "SmartPersistFragment#onAttach end $name. my CoinListComponent: ${myInjector.hashCode()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving && !activity.isChangingConfigurations) {
            (activity as SmartPersistActivity<*>).removedSavedInformation(this.name)
        }
    }

    override fun onDetach() {
        super.onDetach()
        //todo, set navigator to  null?
        smartPersistActivity = null
    }

    fun computeRetainable(): Map<String, Any> {
        val mutableRetainableMap: MutableMap<String, Any> = mutableMapOf()
        mutableRetainableMap.putAll(onRetainableRequested())
        mutableRetainableMap[CHILD_FRAGMENT_NAVIGATOR_KEY] = getChildFragmentNavigatorState()
        return mutableRetainableMap
    }

    override fun onBack(): Boolean {
        Log.d("Cata", "$name-SmartPersistFragment onBack")

        return false
    }

    protected open fun onRetainableRequested(): Map<String, Any> {
        val retainablesMap = mutableMapOf<String, Any>()
        retainablesMap[INJECTOR_KEY] = getInjector()
        return retainablesMap
    }

    fun onBackPressed(): Boolean {
        Log.d("Cata", "$name-SmartPersistFragment onBackPressed")

        var backConsumed: Boolean = childFragmentNavigator.listFragments()
                .filterIsInstance<BackEventAwareComponent>()
                .any { it.onBack() }
        if (!backConsumed) {
            backConsumed = childFragmentNavigator.popBackStackImmediate()
        }

        if (!backConsumed) {
            backConsumed = onBack()
        }

        return backConsumed
    }

    protected fun getInjector(): DaggerComponent {
        return myInjector
    }

    fun isInjectorAvailable(): Boolean {
        return ::myInjector.isInitialized
    }

    fun forceInit(smartPersistActivity: SmartPersistActivity<*>) {
        performFragmentInit(smartPersistActivity)
    }

    abstract fun createInjector(activity: SmartPersistActivity<*>): DaggerComponent

    @Suppress("UNCHECKED_CAST")
    private fun getChildFragmentNavigatorState(): Map<String, Any> {
        return if (::childFragmentNavigator.isInitialized) {
            childFragmentNavigator.retainable
        } else {
            (savedRetainable[CHILD_FRAGMENT_NAVIGATOR_KEY] as Map<String, Any>?).orEmpty()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getChildFragNavigatorRetainable(retainable: Map<String, Any>): Map<String, Any> {
        return (retainable[CHILD_FRAGMENT_NAVIGATOR_KEY] as Map<String, Any>?).orEmpty()
    }

    @Suppress("UNCHECKED_CAST")
    private fun performFragmentInit(smartPersistActivity: SmartPersistActivity<*>) {
        this.smartPersistActivity = smartPersistActivity
        savedRetainable = getComponentHolderFromActivity(this.smartPersistActivity!!)
        initInjector(this.smartPersistActivity!!)
    }

    @Suppress("UNCHECKED_CAST")
    private fun initInjector(activity: SmartPersistActivity<*>) {
        myInjector = getComponentHolderFromActivity(activity)[INJECTOR_KEY] as DaggerComponent? ?: createInjector(activity)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getComponentHolderFromActivity(activity: SmartPersistActivity<*>): Map<String, Any> {
        return ((activity.lastCustomNonConfigurationInstance as Map<String, Any>?)?.get(name) as Map<String, Any>?).orEmpty()
    }

    private companion object {
        private const val CHILD_FRAGMENT_NAVIGATOR_KEY = "fragNav"
        private const val INJECTOR_KEY = "injector"
    }
}