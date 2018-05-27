package com.catalinj.cryptosmart.di.modules.data

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config.CoinMarketCapCoinsRepositoryConfigurator
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
@Module
class RepositoryModule {

    @Provides
    @ApplicationScope
    @CoinMarketCapApiQualifier
    fun provideCoinMarketCapRepository(database: CryptoSmartDb,
                                       @CoinMarketCapApiQualifier retrofit: Retrofit,
                                       userSettings: CryptoSmartUserSettings): CoinsRepository {

        return CoinMarketCapCoinsRepositoryConfigurator(database = database,
                retrofit = retrofit,
                userSettings = userSettings).configure()
    }
}