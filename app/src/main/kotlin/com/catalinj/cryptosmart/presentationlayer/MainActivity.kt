package com.catalinj.cryptosmart.presentationlayer

import android.os.Bundle
import android.util.Log
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinsListFragment
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerActivity<ActivityComponent>(), NamedComponent {

    override val name: String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate.")
        setSupportActionBar(my_toolbar)
        if (savedInstanceState == null) {
            val frag = CoinsListFragment.Factory(activityComponent = injector).create()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, frag, CoinsListFragment.TAG)
                    .commit()
        }
        injector.inject(this)
        Log.d(TAG, "MainActivity${hashCode().toString(16)}#onCreate end. injector: " +
                injector.hashCode().toString(16))
    }

    override fun onCreateDaggerComponent(): ActivityComponent {
        return (application as CryptoSmartApplication).component.getActivityComponent()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity#onStop. isActivityFinishing:$isFinishing " +
                "a2:$isChangingConfigurations")
    }

    private companion object {
        const val TAG = "MainActivity"
    }

}
