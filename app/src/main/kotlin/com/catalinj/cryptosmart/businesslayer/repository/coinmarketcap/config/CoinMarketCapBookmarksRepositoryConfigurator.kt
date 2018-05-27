package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config

import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.CoinMarketCapBookmarksRepository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinjurjiu.common.Configurator
import retrofit2.Retrofit

/**
 * Created by catalin on 03/05/2018.
 */
class CoinMarketCapBookmarksRepositoryConfigurator(private val database: CryptoSmartDb,
                                                   @CoinMarketCapQualifier private val retrofit: Retrofit,
                                                   private val userSettings: CryptoSmartUserSettings)
    : Configurator<BookmarksRepository> {
    override fun configure(): BookmarksRepository {
        val service = retrofit.create(CoinMarketCapService::class.java)
        return CoinMarketCapBookmarksRepository(database, service, userSettings)
    }
}