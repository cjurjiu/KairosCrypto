package com.catalinj.cryptosmart.presentationlayer

import android.os.Bundle
import android.util.Log
import android.view.View
import com.catalinj.cryptosmart.CryptoSmartApplication
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.navigation.impl.DaggerAwareNavigator
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerActivity

class MainActivity : DaggerActivity<ActivityComponent>(), NamedComponent {
    override val name: String = TAG

    val navigator: Navigator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAwareNavigator(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate.")
        if (savedInstanceState == null) {
            navigator.openCoinListScreen()
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

    override fun onBackPressed() {
        if (!navigator.navigateBack()) {
            super.onBackPressed()
        }
    }

    fun showBottomNavigation() {
        findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        findViewById<View>(R.id.bottom_navigation).visibility = View.INVISIBLE
    }

    private companion object {
        const val TAG = "MainActivity"
    }

}
