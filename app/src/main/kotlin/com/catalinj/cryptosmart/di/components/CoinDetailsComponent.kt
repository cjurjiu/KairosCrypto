package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.view.CoinDetailsFragment
import dagger.Subcomponent

/**
 * Created by catalinj on 08.02.2018.
 */
@CoinDetailsScope
@Subcomponent(modules = [(CoinDetailsModule::class)])
abstract class CoinDetailsComponent {

    abstract fun inject(what: CoinDetailsFragment)

    abstract fun getCoinInfoComponent(coinInfoModule: CoinInfoModule): CoinInfoComponent

    abstract fun getCoinMarketsComponent(coinMarketsModule: CoinMarketsModule): CoinMarketsComponent
}