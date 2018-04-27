package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.di.annotations.scopes.CoinListScope
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinsListFragment
import dagger.Subcomponent

/**
 * Created by catalinj on 04.02.2018.
 */
@CoinListScope
@Subcomponent(modules = [CoinListModule::class])
abstract class CoinListComponent {
    abstract fun inject(frag: CoinsListFragment)
}