package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinMarketsScope
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.view.CoinMarketsFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@CoinMarketsScope
@Subcomponent(modules = [CoinMarketsModule::class])
abstract class CoinMarketsComponent {

    abstract fun inject(coinMarketsFragment: CoinMarketsFragment)

    abstract fun inject(scrollToTopWidget: ScrollToTopFloatingActionButton)
}