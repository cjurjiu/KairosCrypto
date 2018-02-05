package com.catalinj.cryptosmart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, CoinsListFragment(), "ListFrag").commit()
        }
        Log.d(TAG, "MainActivity#onCreate end")
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

    override fun onRetainCustomNonConfigurationInstance(): Any {
        val o = Any()
        Log.d(TAG, "MainActivity#onRetainCustomNonConfigurationInstance finishing: $isFinishing. my any: $o")
        return o
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity#onDestroy finishing: $isFinishing")
    }

    private companion object {
        const val TAG = "Cata"
    }
}
