package com.catalinjurjiu.smartpersist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.catalinjurjiu.common.NamedComponent

@Suppress("UNCHECKED_CAST")
abstract class DaggerActivity<ActivityComponent : Any> : FragmentActivity(), NamedComponent {

    lateinit var injector: ActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(name, DaggerComponentStore::class.java) as DaggerComponentStore<ActivityComponent>
        if (!viewModel.hasComp) {
            viewModel.component = onCreateDaggerComponent()
        }
        injector = viewModel.component
        Log.d(TAG, "DaggerActivity${hashCode().toString(16)}#onCreate end. injector: ${injector.hashCode().toString(16)}")
    }

    abstract fun onCreateDaggerComponent(): ActivityComponent

    private companion object {
        const val TAG = "DaggerActivity"
    }
}