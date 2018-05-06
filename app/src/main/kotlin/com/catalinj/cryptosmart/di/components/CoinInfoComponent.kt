package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.CoinInfoScope
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.view.CoinInfoFragment
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@CoinInfoScope
@Subcomponent(modules = [CoinInfoModule::class])
abstract class CoinInfoComponent {
    abstract fun inject(coinInfoFragment: CoinInfoFragment)
}