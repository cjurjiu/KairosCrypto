package com.catalinj.cryptosmart.di

import com.catalinj.cryptosmart.di.components.AppComponent

/**
 * Created by catalinj on 04.02.2018.
 */
interface DependencyRoot {
    fun getAppComponent(): AppComponent
}