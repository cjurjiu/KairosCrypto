package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.di.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.features.coindetails.view.CoinDetailsFragment
import dagger.Subcomponent

/**
 * Created by catalinj on 08.02.2018.
 */
@CoinDetailsScope
@Subcomponent(modules = [(CoinDetailsModule::class)])
abstract class CoinDetailsComponent {
    abstract fun inject(what: CoinDetailsFragment)
}