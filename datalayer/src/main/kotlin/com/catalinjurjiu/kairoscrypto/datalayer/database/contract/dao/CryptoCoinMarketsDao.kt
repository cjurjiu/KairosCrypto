package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import io.reactivex.Flowable

interface CryptoCoinMarketsDao {

    fun getMarketsObservable(coinSymbol: String): Flowable<List<DbCryptoCoinMarketInfo>>

    fun insert(cryptoCoinMarketInfo: DbCryptoCoinMarketInfo): Long

    fun insert(cryptoCoinMarketInfoList: List<DbCryptoCoinMarketInfo>): List<Long>
}