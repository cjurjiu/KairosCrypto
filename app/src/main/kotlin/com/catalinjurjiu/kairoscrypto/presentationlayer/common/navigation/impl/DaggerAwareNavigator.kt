package com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.impl

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.MenuItem
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.presentationlayer.MainActivity
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.view.BookmarksFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.view.CoinDetailsFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view.CoinsListFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.view.SettingsFragment

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
        val frag = CoinsListFragment.Factory(activityComponent = activity.cargo).create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, CoinsListFragment.TAG)
                .commit()
    }

    override fun openBookmarksScreen() {
        val frag = BookmarksFragment.Factory(activityComponent = activity.cargo).create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, BookmarksFragment.TAG)
                .commit()
    }

    override fun openSettingsScreen() {
        val frag = SettingsFragment.SettingsFragmentFactory(activityComponent = activity.cargo)
                .create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, SettingsFragment.TAG)
                .commit()
    }

    override fun openCoinDetailsScreen(cryptoCoin: CryptoCoin) {
        val activityComponent = activity.cargo
        val fragmentFactory = CoinDetailsFragment.Factory(activityComponent = activityComponent,
                cryptoCoin = cryptoCoin)
        val frag = fragmentFactory.create()
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag, CoinDetailsFragment.TAG)
                .addToBackStack(CoinDetailsFragment.TAG)
                .commit()
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
            else -> Log.d("DaggerAwareNavigator", "User selected unknown bottom nav option")
        }
        //we always want to mark the clicked item as selected
        return true
    }

}