package com.catalinjurjiu.kairoscrypto.config

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapBookmarksRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinjurjiu.common.Configurator
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
class CoinMarketCapBookmarksRepositoryConfigurator(private val database: KairosCryptoDb,
                                                   @CoinMarketCapApiQualifier
                                                   private val retrofit: Retrofit)
    : Configurator<BookmarksRepository> {
    override fun configure(): BookmarksRepository {
        val service = retrofit.create(CoinMarketCapApiService::class.java)
        return CoinMarketCapBookmarksRepository(database, service)
    }
}