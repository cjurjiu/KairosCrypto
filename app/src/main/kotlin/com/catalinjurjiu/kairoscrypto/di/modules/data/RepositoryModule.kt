package com.catalinjurjiu.kairoscrypto.di.modules.data

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.config.CoinMarketCapCoinsRepositoryConfigurator
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
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
    fun provideCoinMarketCapRepository(database: KairosCryptoDb,
                                       @CoinMarketCapApiQualifier retrofit: Retrofit): CoinsRepository {

        return CoinMarketCapCoinsRepositoryConfigurator(database = database,
                retrofit = retrofit).configure()
    }
}