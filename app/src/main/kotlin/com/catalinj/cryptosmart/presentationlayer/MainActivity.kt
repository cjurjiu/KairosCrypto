package com.catalinj.cryptosmart.presentationlayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.catalinj.cryptosmart.CryptoSmartApplication
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.modules.activity.ActivityModule
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.navigation.impl.DaggerAwareNavigator
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerActivity
import javax.inject.Inject

class MainActivity : DaggerActivity<ActivityComponent>(), NamedComponent {
    override val name: String = TAG

    val navigator: Navigator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAwareNavigator(this)
    }

    @Inject
    protected lateinit var userSettings: CryptoSmartUserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)

        if (userSettings.isDarkThemeEnabled()) {
            setTheme(R.style.CryptoSmart_Theme_Dark)
        } else {
            setTheme(R.style.CryptoSmart_Theme_Light)
        }

        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate.")
        if (savedInstanceState == null) {
            navigator.openCoinListScreen()
        }
        Log.d(TAG, "MainActivity${hashCode().toString(16)}#onCreate end. injector: " +
                injector.hashCode().toString(16))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateDaggerComponent(): ActivityComponent {
        return (application as CryptoSmartApplication).component
                .getActivityComponent(ActivityModule(activityContext = this))
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
        findViewById<View>(R.id.bottom_navigation).visibility = View.GONE
    }

    fun restart() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private companion object {
        const val TAG = "MainActivity"
    }

}
