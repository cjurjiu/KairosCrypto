package com.catalinj.cryptosmart.di.modules.app

import android.content.Context
import com.catalinj.cryptosmart.di.annotations.scopes.ApplicationScope
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