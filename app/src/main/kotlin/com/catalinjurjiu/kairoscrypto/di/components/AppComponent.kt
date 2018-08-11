package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.KairosCryptoApplication
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import com.catalinjurjiu.kairoscrypto.di.modules.activity.ActivityModule
import com.catalinjurjiu.kairoscrypto.di.modules.app.AppModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.NetworkModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.PersistenceModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.RepositoryModule
import dagger.Component

/**
 * Created by catalinj on 03.02.2018.
 */

@Component(modules = [(AppModule::class),
    (RepositoryModule::class),
    (PersistenceModule::class),
    (NetworkModule::class)])
@ApplicationScope
abstract class AppComponent {

    abstract fun getActivityComponent(module: ActivityModule): ActivityComponent

    abstract fun inject(application: KairosCryptoApplication)
}