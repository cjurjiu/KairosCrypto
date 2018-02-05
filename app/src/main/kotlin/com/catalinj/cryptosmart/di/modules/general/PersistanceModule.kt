package com.catalinj.cryptosmart.di.modules.general

import android.content.Context
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class PersistanceModule {

    @Provides
    @Singleton
    fun provideCryptoSmartDatabase(ctx: Context): CryptoSmartDb {
        return CryptoSmartDb.getInstance(ctx)
    }
}