package com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import io.reactivex.Flowable

/**
 * DAO which provides access to stored [DbPriceData] objects.
 *
 * Created by catalin on 08/05/2018.
 */
@Dao
interface RoomPriceDataDao {

    /**
     * Get a Flowable which monitors the available price data for a specific cryptocurrency.
     *
     * A cryptocurrency can have its value expressed in several other fiat currencies (or Bitcoin).
     * Use [currency] to select the desired representation of value. If all available value representations
     * are desired, pass `"%"`.
     *
     * @param coinSymbol the symbol of the targeted cryptocurrency
     * @param currency the fiat currency (or Bitcoin) in which the value of the cryptocurrency will
     * be expressed. See [CurrencyRepresentation][com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation]
     * for possible values.
     *
     * By default fetches all the available value representations.
     */
    @Query("SELECT * FROM ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " WHERE ${DbPriceData.ColumnNames.COIN_SYMBOL} = :coinSymbol" +
            " AND ${DbPriceData.ColumnNames.CURRENCY} LIKE :currency")
    fun getPriceDataFlowable(coinSymbol: String, currency: String = "%"): Flowable<List<DbPriceData>>

    /**
     * Get the list of available [DbPriceData] items for a specific cryptocurrency.
     *
     * A cryptocurrency can have its value expressed in several other fiat currencies (or Bitcoin).
     * Use [currency] to select the desired representation of value. If all available value representations
     * are desired, pass `"%"`.
     *
     * @param coinSymbol the symbol of the targeted cryptocurrency
     * @param currency the fiat currency (or Bitcoin) in which the value of the cryptocurrency will
     * be expressed. See [CurrencyRepresentation][com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation]
     * for possible values.
     *
     * By default fetches all the available value representations.
     */
    @Query("SELECT * FROM ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " WHERE ${DbPriceData.ColumnNames.COIN_SYMBOL} LIKE :coinSymbol" +
            " AND ${DbPriceData.ColumnNames.CURRENCY} LIKE :currency")
    fun getPriceData(coinSymbol: String, currency: String = "%"): List<DbPriceData>

    /**
     * Inserts a list of [DbPriceData]s in the database, in the [DbPriceData.PRICE_DATA_TABLE_NAME] table.
     *
     * If one of the entries already exists(checked by the primary key), the insert already existing
     * item is overwritten by the new one.
     *
     * @param coinsPriceData crypto coin price data objects to be added.
     * @return a list of long id's which represent the row ids of the inserted rows
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coinsPriceData: List<DbPriceData>): List<Long>
}