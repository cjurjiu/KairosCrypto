package com.catalinjurjiu.kairoscrypto.config

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.MarketsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapMarketsRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinjurjiu.common.Configurator
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
class CoinMarketCapMarketsRepositoryConfigurator(private val database: KairosCryptoDb,
                                                 @CoinMarketCapHtmlQualifier private val retrofit: Retrofit,
                                                 private val userSettings: KairosCryptoUserSettings)
    : Configurator<MarketsRepository> {

    override fun configure(): MarketsRepository {
        val service = retrofit.create(CoinMarketCapHtmlService::class.java)
        return CoinMarketCapMarketsRepository(kairosCryptoDb = database,
                coinMarketCapHtmlService = service,
                userSettings = userSettings)
    }
}