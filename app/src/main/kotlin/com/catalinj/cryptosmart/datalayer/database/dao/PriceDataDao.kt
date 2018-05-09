package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.*
import com.catalinj.cryptosmart.datalayer.database.models.DbPriceData
import io.reactivex.Flowable

/**
 * Created by catalin on 08/05/2018.
 */
@Dao
interface PriceDataDao {
    @Query("SELECT * FROM ${DbPriceData.PRICE_DATA_TABLE_NAME}")
    fun getAll(): List<DbPriceData>

    @Query("SELECT * FROM ${DbPriceData.PRICE_DATA_TABLE_NAME}")
    fun monitorRx(): Flowable<List<DbPriceData>>

    @Query("SELECT * FROM ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " WHERE ${DbPriceData.ColumnNames.COIN_SYMBOL} LIKE :cryptoCoinSymbol" +
            " AND ${DbPriceData.ColumnNames.CURRENCY} LIKE :desiredCurrency")
    fun getPriceForCrypto(cryptoCoinSymbol: String, desiredCurrency: String): List<DbPriceData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coinsPriceData: List<DbPriceData>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coinsPriceData: List<DbPriceData>)
}