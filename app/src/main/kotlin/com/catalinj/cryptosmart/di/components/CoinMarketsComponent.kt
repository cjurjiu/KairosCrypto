package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.CoinMarketsScope
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view.CoinMarketsFragment
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@CoinMarketsScope
@Subcomponent(modules = [CoinMarketsModule::class])
abstract class CoinMarketsComponent {

    abstract fun inject(coinMarketsFragment: CoinMarketsFragment)
}