package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinInfoScope
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.view.CoinInfoFragment
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@CoinInfoScope
@Subcomponent(modules = [CoinInfoModule::class])
abstract class CoinInfoComponent {
    abstract fun inject(coinInfoFragment: CoinInfoFragment)
}