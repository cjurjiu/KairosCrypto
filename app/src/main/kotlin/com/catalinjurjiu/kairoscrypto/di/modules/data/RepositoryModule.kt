package com.catalinjurjiu.kairoscrypto.di.modules.data

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapCoinsRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.RestServiceFactory
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 03/05/2018.
 */
@Module
class RepositoryModule {

    @Provides
    @ApplicationScope
    fun provideCoinMarketCapRepository(database: KairosCryptoDb,
                                       restServiceFactory: RestServiceFactory): CoinsRepository {

        return CoinMarketCapCoinsRepository(kairosCryptoDb = database,
                coinMarketCapApiService = restServiceFactory.getCoinsRestServiceApi())
    }
}