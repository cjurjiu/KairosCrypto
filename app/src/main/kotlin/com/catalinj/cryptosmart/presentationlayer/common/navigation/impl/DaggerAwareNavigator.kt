package com.catalinj.cryptosmart.presentationlayer.common.navigation.impl

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.MenuItem
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view.BookmarksFragment
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.view.CoinDetailsFragment
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinsListFragment
import com.catalinj.cryptosmart.presentationlayer.features.settings.SettingsFragment

/**
 * Created by catalin on 27/04/2018.
 */
class DaggerAwareNavigator(private val activity: MainActivity) : Navigator {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    private val bottomNavigationView: BottomNavigationView = activity.findViewById(R.id.bottom_navigation)

    init {
        bottomNavigationView.selectedItemId = R.id.coin_list
        bottomNavigationView.setOnNavigationItemSelectedListener { handleBottomNavigationClick(it) }
        bottomNavigationView.setOnNavigationItemReselectedListener { /*nothing, do nothing*/ }
    }

    override fun openCoinListScreen() {
        val frag = CoinsListFragment.Factory(activityComponent = activity.injector).create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, CoinsListFragment.TAG)
                .commit()
        activity.showBottomNavigation()
    }

    override fun openBookmarksScreen() {
        val frag = BookmarksFragment.Factory(activityComponent = activity.injector).create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, BookmarksFragment.TAG)
                .commit()
        activity.showBottomNavigation()
    }

    override fun openSettingsScreen() {
        val frag = SettingsFragment()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, SettingsFragment.TAG)
                .commit()
        activity.showBottomNavigation()
    }

    override fun openCoinDetailsScreen(cryptoCoin: CryptoCoin) {
        val activityComponent = activity.injector
        val fragmentFactory = CoinDetailsFragment.Factory(activityComponent = activityComponent,
                cryptoCoin = cryptoCoin)
        val frag = fragmentFactory.create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, CoinDetailsFragment.TAG)
                .addToBackStack(CoinDetailsFragment.TAG)
                .commit()
        activity.hideBottomNavigation()
    }

    override fun navigateBack(): Boolean {
        return fragmentManager.fragments
                .filter { it.isVisible }
                .filterIsInstance(BackEventAwareComponent::class.java)
                .firstOrNull { it.onBack() }
                ?.let { true } ?: fragmentManager.popBackStackImmediate()
    }

    private fun handleBottomNavigationClick(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.bookmarks -> openBookmarksScreen()
            R.id.coin_list -> openCoinListScreen()
            R.id.settings -> openSettingsScreen()
            else -> Log.d("Cata", "User selected unknown bottom nav option")
        }
        //we always want to mark the clicked item as selected
        return true
    }

}