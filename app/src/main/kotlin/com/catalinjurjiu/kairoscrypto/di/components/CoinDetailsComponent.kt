package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinDetailsScope
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.CoinDetailsModule
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.view.CoinDetailsFragment
import dagger.Subcomponent

/**
 * Created by catalinj on 08.02.2018.
 */
@CoinDetailsScope
@Subcomponent(modules = [CoinDetailsModule::class])
abstract class CoinDetailsComponent {

    abstract fun inject(what: CoinDetailsFragment)

    abstract fun getCoinInfoComponent(coinInfoModule: CoinInfoModule): CoinInfoComponent

    abstract fun getCoinMarketsComponent(coinMarketsModule: CoinMarketsModule): CoinMarketsComponent
}