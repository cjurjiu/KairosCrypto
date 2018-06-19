package com.catalinjurjiu.wheelbarrow

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.store.InjectorStore

/**
 * T is the type of the Injector to be stored during a configuration change.
 */
@Suppress("UNCHECKED_CAST")
abstract class InjectorActivity<InjectorType : Any> : AppCompatActivity(), NamedComponent {

    lateinit var injector: InjectorType
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(name, InjectorStore::class.java) as InjectorStore<InjectorType>
        if (!viewModel.hasComp) {
            viewModel.component = onCreateInjector()
        }
        injector = viewModel.component
        Log.d(TAG, "InjectorActivity${hashCode().toString(16)}#onCreate end. injector: ${injector.hashCode().toString(16)}")
    }

    /**
     * Called when the Injector needs to be created.
     */
    abstract fun onCreateInjector(): InjectorType

    private companion object {
        const val TAG = "InjectorActivity"
    }
}