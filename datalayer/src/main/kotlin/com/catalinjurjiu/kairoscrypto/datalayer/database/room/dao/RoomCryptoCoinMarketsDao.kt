package com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import io.reactivex.Flowable

/**
 * DAO which provides access to stored [DbCryptoCoinMarketInfo] objects.
 */
@Dao
interface RoomCryptoCoinMarketsDao {

    /**
     * Get a Flowable which monitors all known markets for the coin identified by [coinSymbol].
     *
     * Whenever a change occurs on one of the known markets, a new list is emitted by the returned
     * Flowable.
     *
     * @param coinSymbol the coin symbol for which the markets need to be fetched (e.g. BTC for Bitcoin)
     * @return a [Flowable] which monitors all the known markets for the desired coin.
     */
    @Query(value = "SELECT * FROM ${DbCryptoCoinMarketInfo.TABLE_NAME}" +
            " WHERE ${DbCryptoCoinMarketInfo.ColumnNames.COIN_SYMBOL} = :coinSymbol" +
            " ORDER BY ${DbCryptoCoinMarketInfo.ColumnNames.RANK} ASC")
    fun getMarketsObservable(coinSymbol: String): Flowable<List<DbCryptoCoinMarketInfo>>

    /**
     * Inserts a [DbCryptoCoinMarketInfo] in the database, in the [DbCryptoCoinMarketInfo.TABLE_NAME] table.
     *
     * If one of the entries already exists(checked by the primary key), the insert already existing
     * item is overwritten by the new one.
     *
     * @param cryptoCoinMarketInfo coin market info object to be added.
     * @return a long which represents the row id of the inserted row
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoCoinMarketInfo: DbCryptoCoinMarketInfo): Long

    /**
     * Inserts a list of [DbCryptoCoinMarketInfo]s in the database, in the [DbCryptoCoinMarketInfo.TABLE_NAME] table.
     *
     * If one of the entries already exists(checked by the primary key), the insert already existing
     * item is overwritten by the new one.
     *
     * @param cryptoCoinMarketInfoList coin market info objects to be added.
     * @return a list of long id's which represent the row ids of the inserted rows
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoCoinMarketInfoList: List<DbCryptoCoinMarketInfo>): List<Long>
}