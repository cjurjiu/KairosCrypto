package com.catalinjurjiu.kairoscrypto.di.modules.data

import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettingsImpl
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
    fun provideKairosCryptoDatabase(ctx: Context): KairosCryptoDb {
        return KairosCryptoDb.getInstance(ctx)
    }

    @Provides
    @ApplicationScope
    fun provideSettings(ctx: Context): KairosCryptoUserSettings {
        return KairosCryptoUserSettingsImpl(ctx)
    }
}