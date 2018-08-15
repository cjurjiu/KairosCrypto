package com.catalinjurjiu.kairoscrypto.datalayer.database.contract

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.*

/**
 * Database definition of the Kairos Crypto application.
 *
 * Provides access to the following DAOs:
 *   * [CryptoCoinDao];
 *   * [PartialCryptoCoinDao];
 *   * [PriceDataDao];
 *   * [BookmarksDao];
 *   * [CryptoCoinMarketsDao].
 *
 * Created by catalinj on 27.01.2018.
 */
interface KairosCryptoDb {
    fun getCryptoCoinDao(): CryptoCoinDao

    fun getPlainCryptoCoinDao(): PartialCryptoCoinDao

    fun getCoinMarketCapPriceDataDao(): PriceDataDao

    fun getBookmarksDao(): BookmarksDao

    fun getMarketsInfoDao(): CryptoCoinMarketsDao
}