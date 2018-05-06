package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.view.CoinInfoFragment
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view.CoinMarketsFragment

/**
 * Created by catalin on 05/05/2018.
 */
class CoinDetailsViewPagerAdapter(private val coinId: String,
                                  private val coinSymbol: String,
                                  private val coinDetailsComponent: CoinDetailsComponent,
                                  supportFragmentManager: FragmentManager)
    : FragmentPagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            CoinInfoFragment.Factory(coinId = coinId,
                    coinSymbol = coinSymbol,
                    coinDetailsComponent = coinDetailsComponent)
                    .create()
        } else {
            CoinMarketsFragment.Factory(coinId = coinId,
                    coinSymbol = coinSymbol,
                    coinDetailsComponent = coinDetailsComponent)
                    .create()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Overview"
        } else {
            "Markets"
        }
    }

    override fun getCount(): Int {
        return 2
    }
}