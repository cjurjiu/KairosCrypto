package com.catalinjurjiu.kairoscrypto.di.modules.data

import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDbFactory
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettingsImpl
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.ApplicationContext
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class PersistenceModule {

    @Provides
    @ApplicationScope
    fun provideKairosCryptoDatabase(@ApplicationContext ctx: Context): KairosCryptoDb {
        return KairosCryptoDbFactory.getDatabase(ctx)
    }

    @Provides
    @ApplicationScope
    fun provideSettings(@ApplicationContext ctx: Context): KairosCryptoUserSettings {
        return KairosCryptoUserSettingsImpl(ctx)
    }
}