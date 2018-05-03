package com.catalinj.cryptosmart.di.modules.data

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config.CoinMarketCapRepositoryConfigurator
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by catalin on 03/05/2018.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    @CoinMarketCapQualifier
    fun provideCoinMarketCapRepository(database: CryptoSmartDb,
                                       @CoinMarketCapQualifier retrofit: Retrofit): CoinsRepository {
        return CoinMarketCapRepositoryConfigurator(database = database, retrofit = retrofit)
                .configure()
    }
}