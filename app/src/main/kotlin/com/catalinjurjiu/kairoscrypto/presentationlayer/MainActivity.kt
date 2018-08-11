package com.catalinjurjiu.kairoscrypto.presentationlayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.catalinjurjiu.kairoscrypto.KairosCryptoApplication
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.components.ActivityComponent
import com.catalinjurjiu.kairoscrypto.di.modules.activity.ActivityModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.impl.DaggerAwareNavigator
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.WheelbarrowActivity
import javax.inject.Inject

class MainActivity : WheelbarrowActivity<ActivityComponent>(), NamedComponent {
    override val name: String = TAG

    //make cargo public
    public override val cargo: ActivityComponent by lazy(LazyThreadSafetyMode.NONE) { super.cargo }

    val navigator: Navigator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAwareNavigator(this)
    }

    @Inject
    protected lateinit var userSettings: KairosCryptoUserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(this)

        if (userSettings.isDarkThemeEnabled()) {
            setTheme(R.style.KairosCrypto_Theme_Dark)
        } else {
            setTheme(R.style.KairosCrypto_Theme_Light)
        }

        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate.")
        if (savedInstanceState == null) {
            navigator.openCoinListScreen()
        }
        Log.d(TAG, "MainActivity${hashCode().toString(16)}#onCreate end. injector: " +
                cargo.hashCode().toString(16))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateCargo(): ActivityComponent {
        return (application as KairosCryptoApplication).component
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