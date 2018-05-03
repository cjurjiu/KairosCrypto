package com.catalinj.cryptosmart.di.modules.data

import android.content.Context
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class PersistenceModule {

    @Provides
    @ApplicationScope
    fun provideCryptoSmartDatabase(ctx: Context): CryptoSmartDb {
        return CryptoSmartDb.getInstance(ctx)
    }
}