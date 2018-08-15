package com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import io.reactivex.Flowable

/**
 * DAO which provides access to stored [DbCryptoCoin] objects.
 *
 * Created by catalin on 09/05/2018.
 */
@Dao
interface RoomCryptoCoinDao {

    /**
     * Gets all available [DbCryptoCoin]s, ordered by rank, ascending.
     *
     * @return a list of [DbCryptoCoin], ordered ascending by rank.
     */
    @Transaction
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors all known coins.
     *
     * Whenever a change occurs on one of the known coins, a new list is emitted by the returned
     * Flowable.
     *
     * @return a [Flowable] which monitors all known coins for changes.
     */
    @Transaction
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getCryptoCoinsFlowable(): Flowable<List<DbCryptoCoin>>

    /**
     * Get a Flowable which monitors all known coins which have their currency represented in [currency].
     *
     * Whenever a change occurs on one of the known coins, a new list is emitted by the returned
     * Flowable.
     *
     * @param currency a String obtained from a [CurrencyRepresentation][com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation]
     * representing the currency in which the monitored coins need to be represented.
     * @return a [Flowable] which monitors all known coins with values as [currency] for changes.
     */
    @Transaction
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " ON ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = " +
            "${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.COIN_SYMBOL}" +
            " WHERE ${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.CURRENCY} = :currency" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getCryptoCoinsFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    /**
     * Get a Flowable which monitors a specific crypto coin.
     *
     * @param coinSymbol the symbol of the cryptocurrency to track
     */
    @Transaction
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " WHERE ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = :coinSymbol" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getSingleCoinFlowable(coinSymbol: String): Flowable<DbCryptoCoin>
}