package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoinMarketInfo
import io.reactivex.Flowable

@Dao
interface CryptoCoinMarketsDao {

    @Query(value = "SELECT * FROM ${DbCryptoCoinMarketInfo.TABLE_NAME}" +
            " WHERE ${DbCryptoCoinMarketInfo.ColumnNames.COIN_SYMBOL} = :coinSymbol" +
            " ORDER BY ${DbCryptoCoinMarketInfo.ColumnNames.RANK} ASC")
    fun getMarketsObservable(coinSymbol: String): Flowable<List<DbCryptoCoinMarketInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoCoinMarketInfo: DbCryptoCoinMarketInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoCoinMarketInfoList: List<DbCryptoCoinMarketInfo>): List<Long>
}