package com.catalinj.cryptosmart.config

import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.CoinMarketCapMarketsRepository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinjurjiu.common.Configurator
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
class CoinMarketCapMarketsRepositoryConfigurator(private val database: CryptoSmartDb,
                                                 @CoinMarketCapHtmlQualifier private val retrofit: Retrofit,
                                                 private val userSettings: CryptoSmartUserSettings)
    : Configurator<MarketsRepository> {

    override fun configure(): MarketsRepository {
        val service = retrofit.create(CoinMarketCapHtmlService::class.java)
        return CoinMarketCapMarketsRepository(cryptoSmartDb = database,
                coinMarketCapHtmlService = service,
                userSettings = userSettings)
    }
}