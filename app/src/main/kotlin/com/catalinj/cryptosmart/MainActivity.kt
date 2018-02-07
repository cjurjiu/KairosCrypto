package com.catalinj.cryptosmart

import android.os.Bundle
import android.util.Log
import com.catalinj.cryptosmart.common.BaseActivity
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment
import javax.inject.Provider

class MainActivity : BaseActivity<ActivityComponent>(), Provider<ActivityComponent> {

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

    override fun get(): ActivityComponent {
        return getInjector()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d(TAG, "MainActivity#onPostCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity#onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity#onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity#onPause finishing: $isFinishing")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "MainActivity#onSaveInstanceState finishing: $isFinishing")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity#onStop finishing: $isFinishing")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity#onDestroy finishing: $isFinishing")
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}
