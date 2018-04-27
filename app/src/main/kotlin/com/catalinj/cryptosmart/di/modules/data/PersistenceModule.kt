package com.catalinj.cryptosmart.di.modules.data

import android.content.Context
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideCryptoSmartDatabase(ctx: Context): CryptoSmartDb {
        return CryptoSmartDb.getInstance(ctx)
    }
}