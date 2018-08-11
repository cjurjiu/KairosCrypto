package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinListScope
import com.catalinjurjiu.kairoscrypto.di.modules.coinlist.CoinListModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view.CoinsListFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import dagger.Subcomponent

/**
 * Created by catalinj on 04.02.2018.
 */
@CoinListScope
@Subcomponent(modules = [CoinListModule::class])
abstract class CoinListComponent {
    abstract fun inject(frag: CoinsListFragment)

    abstract fun inject(toolbar: CoinDisplayOptionsToolbar)

    abstract fun inject(scrollToTopWidget: ScrollToTopFloatingActionButton)
}