package com.catalinj.cryptosmart.di.modules.general

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class AppModule(_context: Context) {
    private val context: Context = _context.applicationContext

    @Provides
    fun provideAppContext(): Context {
        return context
    }
}