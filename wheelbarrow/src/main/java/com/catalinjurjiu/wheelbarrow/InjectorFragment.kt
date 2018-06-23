package com.catalinjurjiu.wheelbarrow

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.catalinjurjiu.common.Factory
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.log.Chronicle
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
        if (doPersistInjectorToViewModel) {
            if (isInjectorInitialized()) {
                injectorStore.component = injector
                Chronicle.logDebug(this::class.java.simpleName, "Saved injector " +
                        "${injectorStore.hashCode().toString(16)} for " +
                        "${this.javaClass.simpleName}${this.hashCode().toString(16)}.")
            } else {
                //Injector not initialised but the flags say that the injector was initialised and
                //needs to be stored. Decide how to handle this...maybe just throw an Exception?
                Chronicle.logError(this::class.java.simpleName, "Cannot store an " +
                        "uninitialised injector.")
            }
        } else if (doReadInjectorFromViewModel) {
            if (injectorStore.hasComp) {
                injector = injectorStore.component
                Chronicle.logDebug(this::class.java.simpleName, "Re-initialised injector " +
                        "${injectorStore.hashCode().toString(16)} for " +
                        "${this.javaClass.simpleName}${this.hashCode().toString(16)}.")
            } else {
                //Injector wants to be initialized from the injector store, but the store has no
                //injector in it....an error? check flags!
                Chronicle.logError(this::class.java.simpleName, "Cannot initialize the " +
                        "injector from an empty injector store.")
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