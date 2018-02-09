package com.catalinj.cryptosmart

import android.os.Bundle
import android.util.Log
import com.catalinj.cryptosmart.common.cryptobase.BaseActivity
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
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, CoinsListFragment(), CoinsListFragment.TAG).commit()
        }
        val activityComponent = getInjector().inject(this)
        Log.d(TAG, "MainActivity#onCreate end. activityComponent: $activityComponent")
    }

    override fun getIdentity(): String {
        return TAG
    }

    override fun createInjector(): ActivityComponent {
        return (application as CryptoSmartApplication).getAppComponent().getActivityComponent()
    }

    override fun retainsFragments(): Boolean {
        return true
    }

    fun getCoinListComponent(): CoinListComponent {
        return getInjector().getCoinListComponent(CoinListModule())
    }

    private companion object {
        const val TAG = "MainActivity"
    }

    fun getCoinDetailsComponent(): CoinDetailsComponent {
        return getInjector().getCoinDetailsComponent(CoinDetailsModule())
    }

}
