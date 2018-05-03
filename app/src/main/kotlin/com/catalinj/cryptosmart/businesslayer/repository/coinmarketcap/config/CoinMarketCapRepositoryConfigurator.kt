package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.CoinMarketCapCoinsRepository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinjurjiu.common.Configurator
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
class CoinMarketCapRepositoryConfigurator(val database: CryptoSmartDb,
                                          @CoinMarketCapQualifier val retrofit: Retrofit)
    : Configurator<CoinsRepository> {
    override fun configure(): CoinsRepository {
        val service = retrofit.create(CoinMarketCapService::class.java)
        return CoinMarketCapCoinsRepository(database, service)
    }
}