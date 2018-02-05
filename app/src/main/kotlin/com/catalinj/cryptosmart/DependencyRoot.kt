package com.catalinj.cryptosmart

import com.catalinj.cryptosmart.di.components.AppComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent

/**
 * Created by catalinj on 04.02.2018.
 */
interface DependencyRoot {
    fun getAppComponent(): AppComponent

    fun getCoinListComponent(): CoinListComponent
}