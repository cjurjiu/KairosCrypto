package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.modules.general.AppModule
import com.catalinj.cryptosmart.di.modules.general.NetworkModule
import com.catalinj.cryptosmart.di.modules.general.PersistenceModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by catalinj on 03.02.2018.
 */

@Component(modules = [(AppModule::class),
    (PersistenceModule::class),
    (NetworkModule::class)])
@Singleton
abstract class AppComponent {

    abstract fun getActivityComponent(): ActivityComponent
}