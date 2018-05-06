package com.catalinjurjiu.smartpersist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.catalinjurjiu.common.Factory
import com.catalinjurjiu.common.NamedComponent

/**
 * T is the type of the Dagger component to be stored during a configuration change.
 *
 * Name is used to identify the fragment in the ViewModelProvider's store. The Fragment TAG should be
 * enough.
 * Created by catalinj on 27.02.2018.
 */
@Suppress("UNCHECKED_CAST")
abstract class DaggerFragment<T : Any> : Fragment(), NamedComponent {

    private var doPersistDaggerCompToViewModel: Boolean = false
    private var doReadDaggerCompToViewModel: Boolean = true
    protected lateinit var injector: T
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()
    }

    private fun initializeInjector() {
        val daggerComponentStore = ViewModelProviders.of(this).get(name, DaggerComponentStore::class.java)
                as DaggerComponentStore<T>
        Log.d("Cata", "As ${this.javaClass.simpleName}${this.hashCode().toString(16)}Got dagger componentStore: ${daggerComponentStore.hashCode().toString(16)}")
        if (doPersistDaggerCompToViewModel) {
            if (isInjectorInitialized()) {
                daggerComponentStore.component = injector
            } else {
                Log.e("Cata", "injector not initialised but the flags were set to store the initializer?? how??")
            }
        } else if (doReadDaggerCompToViewModel) {
            if (daggerComponentStore.hasComp) {
                injector = daggerComponentStore.component
            } else {
                Log.e("Cata", "injector wants to be initialized from the dagger comp store, but the store has no\n" +
                        "component in it....an error? check flags? this class ${this::class.simpleName}")
            }
        }
    }

    private fun isInjectorInitialized(): Boolean {
        return ::injector.isInitialized
    }

    abstract class DaggerFragmentFactory<DaggerComponent : Any> : Factory<DaggerFragment<DaggerComponent>> {

        final override fun create(): DaggerFragment<DaggerComponent> {
            val f: DaggerFragment<DaggerComponent> = onCreateFragment()
            val daggerComponent: DaggerComponent = onCreateDaggerComponent()
            f.injector = daggerComponent
            f.doPersistDaggerCompToViewModel = true
            f.doReadDaggerCompToViewModel = false
            return f
        }

        abstract fun onCreateFragment(): DaggerFragment<DaggerComponent>

        abstract fun onCreateDaggerComponent(): DaggerComponent
    }

}