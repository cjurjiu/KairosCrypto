package com.catalinjurjiu.kairoscrypto.di.modules.app

import android.content.Context
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class AppModule(appContext: Context) {
    private val context: Context = appContext.applicationContext

    @Provides
    @ApplicationScope
    fun provideAppContext(): Context {
        return context
    }
}