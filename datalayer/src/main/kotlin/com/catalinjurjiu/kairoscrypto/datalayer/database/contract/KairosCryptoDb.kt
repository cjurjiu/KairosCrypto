package com.catalinjurjiu.kairoscrypto.datalayer.database.contract

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.*

interface KairosCryptoDb {
    fun getCryptoCoinDao(): CryptoCoinDao

    fun getPlainCryptoCoinDao(): PartialCryptoCoinDao

    fun getCoinMarketCapPriceDataDao(): PriceDataDao

    fun getBookmarksDao(): BookmarksDao

    fun getMarketsInfoDao(): CryptoCoinMarketsDao
}