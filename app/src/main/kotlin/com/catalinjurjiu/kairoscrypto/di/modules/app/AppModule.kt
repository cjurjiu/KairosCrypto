package com.catalinjurjiu.kairoscrypto.di.modules.app

import android.content.Context
import android.util.Log
import com.catalinjurjiu.common.ActiveActivityProvider
import com.catalinjurjiu.kairoscrypto.KairosCryptoApplication
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.ApplicationContext
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class AppModule(application: KairosCryptoApplication) {
    private val context: Context = application.applicationContext
    private val activeActivityProvider = ActiveActivityProvider(application = application)

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideAppContext(): Context {
        return context
    }

    @Provides
    @ApplicationScope
    fun provideActiveActivityProvider(): ActiveActivityProvider {
        return activeActivityProvider
    }
}