package com.catalinj.cryptosmart

import android.os.Bundle
import android.util.Log
import com.catalinj.cryptosmart.common.cryptobase.BaseActivity
import com.catalinj.cryptosmart.common.cryptobase.FragmentNavigator
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment

class MainActivity : BaseActivity<ActivityComponent>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity#onCreate.")

        if (savedInstanceState == null) {
            FragmentNavigator.doInit(supportFragmentManager)
            FragmentNavigator.instance.add(R.id.fragment_container, CoinsListFragment(), CoinsListFragment.TAG)
        } else {
            FragmentNavigator.updateFragManager(supportFragmentManager)
        }

        getInjector().inject(this)
        Log.d(TAG, "MainActivity${hashCode()}#onCreate end. injector: ${getInjector().hashCode()}")
    }

    override fun getIdentity(): String {
        return TAG
    }

    override fun createInjector(): ActivityComponent {
        return (application as CryptoSmartApplication).getAppComponent().getActivityComponent()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity#onStop. isActivityFinishing:$isFinishing " +
                "a2:$isChangingConfigurations")
    }

    override fun retainsFragments(): Boolean {
        return true
    }

    fun getCoinListComponent(): CoinListComponent {
        return getInjector().getCoinListComponent(CoinListModule())
    }

    fun getCoinDetailsComponent(): CoinDetailsComponent {
        return getInjector().getCoinDetailsComponent(CoinDetailsModule())
    }

    private companion object {
        const val TAG = "MainActivity"
    }

}
