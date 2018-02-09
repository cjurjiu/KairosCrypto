package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.di.scopes.CoinListScope
import com.catalinj.cryptosmart.features.coinslist.view.CoinsListFragment
import dagger.Subcomponent

/**
 * Created by catalinj on 04.02.2018.
 */
@CoinListScope
@Subcomponent(modules = [CoinListModule::class])
abstract class CoinListComponent {
    abstract fun inject(frag: CoinsListFragment)
}