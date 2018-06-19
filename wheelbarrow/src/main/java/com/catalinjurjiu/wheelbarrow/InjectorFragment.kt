package com.catalinjurjiu.wheelbarrow

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.catalinjurjiu.common.Factory
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.store.InjectorStore

/**
 * T is the type of the Injector to be stored during a configuration change.
 *
 * Name is used to identify the fragment in the ViewModelProvider's store. The Fragment TAG should be
 * enough.
 *
 * Created by catalinj on 27.02.2018.
 */
@Suppress("UNCHECKED_CAST")
abstract class InjectorFragment<InjectorType : Any> : Fragment(), NamedComponent {

    private var doPersistInjectorToViewModel: Boolean = false
    private var doReadInjectorFromViewModel: Boolean = true
    protected lateinit var injector: InjectorType
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()
    }

    private fun initializeInjector() {
        val injectorStore = ViewModelProviders.of(this).get(name, InjectorStore::class.java)
                as InjectorStore<InjectorType>
        Log.d("Cata", "As ${this.javaClass.simpleName}${this.hashCode().toString(16)}Got Injector componentStore: ${injectorStore.hashCode().toString(16)}")
        if (doPersistInjectorToViewModel) {
            if (isInjectorInitialized()) {
                injectorStore.component = injector
            } else {
                Log.e("Cata", "injector not initialised but the flags were set to store the initializer?? how??")
            }
        } else if (doReadInjectorFromViewModel) {
            if (injectorStore.hasComp) {
                injector = injectorStore.component
            } else {
                Log.e("Cata", "injector wants to be initialized from the dagger comp store, but the store has no\n" +
                        "component in it....an error? check flags? this class ${this::class.simpleName}")
            }
        }
    }

    private fun isInjectorInitialized(): Boolean {
        return ::injector.isInitialized
    }

    abstract class InjectorFragmentFactory<InjectorType : Any> : Factory<InjectorFragment<InjectorType>> {

        final override fun create(): InjectorFragment<InjectorType> {
            val f: InjectorFragment<InjectorType> = onCreateFragment()
            val daggerComponent: InjectorType = onCreateInjector()
            f.injector = daggerComponent
            f.doPersistInjectorToViewModel = true
            f.doReadInjectorFromViewModel = false
            return f
        }

        abstract fun onCreateFragment(): InjectorFragment<InjectorType>

        abstract fun onCreateInjector(): InjectorType
    }

}