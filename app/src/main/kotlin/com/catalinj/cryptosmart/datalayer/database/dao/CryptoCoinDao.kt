package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbPriceData
import io.reactivex.Flowable

/**
 * Created by catalin on 09/05/2018.
 */
@Dao
interface CryptoCoinDao {

    /**
     * Gets all available [DbCryptoCoin]s, ordered by rank, ascending.
     */
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " ON ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = " +
            "${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.COIN_SYMBOL}" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors the list of known coins.
     */
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " ON ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = " +
            "${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.COIN_SYMBOL}" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getCryptoCoinsFlowable(): Flowable<List<DbCryptoCoin>>

    /**
     * Get a Flowable which monitors a specific crypto coin.
     *
     * @param coinSymbol the symbol of the cryptocurrency to track
     */
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " ON ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = " +
            "${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.COIN_SYMBOL}" +
            " WHERE ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL} = :coinSymbol" +
            " ORDER BY ${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.RANK}")
    fun getSingleCoinFlowable(coinSymbol: String): Flowable<DbCryptoCoin>
}