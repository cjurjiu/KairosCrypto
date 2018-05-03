package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.modules.app.AppModule
import com.catalinj.cryptosmart.di.modules.data.NetworkModule
import com.catalinj.cryptosmart.di.modules.data.PersistenceModule
import com.catalinj.cryptosmart.di.modules.data.RepositoryModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by catalinj on 03.02.2018.
 */

@Component(modules = [(AppModule::class),
    (RepositoryModule::class),
    (PersistenceModule::class),
    (NetworkModule::class)])
@Singleton
abstract class AppComponent {

    abstract fun getActivityComponent(): ActivityComponent
}