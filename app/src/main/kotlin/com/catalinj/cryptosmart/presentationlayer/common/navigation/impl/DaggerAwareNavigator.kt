package com.catalinj.cryptosmart.presentationlayer.common.navigation.impl

import android.support.v4.app.FragmentManager
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.view.CoinDetailsFragment
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinsListFragment
import com.catalinjurjiu.smartpersist.DaggerActivity

/**
 * Created by catalin on 27/04/2018.
 */
class DaggerAwareNavigator(private val activity: DaggerActivity<ActivityComponent>) : Navigator {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun openCoinListScreen() {
        val frag = CoinsListFragment.Factory(activityComponent = activity.injector).create()
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, frag, CoinsListFragment.TAG)
                .commit()
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
    }
}