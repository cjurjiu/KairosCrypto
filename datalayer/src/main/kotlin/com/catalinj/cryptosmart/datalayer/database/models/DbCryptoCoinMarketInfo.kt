package com.catalinj.cryptosmart.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Represents one entry in the markets table
 */
@Entity(tableName = DbCryptoCoinMarketInfo.TABLE_NAME,
        primaryKeys = [DbCryptoCoinMarketInfo.ColumnNames.COIN_SYMBOL,
            DbCryptoCoinMarketInfo.ColumnNames.RANK])
data class DbCryptoCoinMarketInfo(@ColumnInfo(name = ColumnNames.RANK)
                                  val rank: Int,
                                  @ColumnInfo(name = ColumnNames.EXCHANGE_NAME)
                                  val exchangeName: String,
                                  @ColumnInfo(name = ColumnNames.EXCHANGE_PAIR_URL)
                                  val exchangePairUrl: String,
                                  @ColumnInfo(name = ColumnNames.COIN_SYMBOL)
                                  val coinSymbol: String,
                                  @ColumnInfo(name = ColumnNames.EXCHANGE_PAIR_SYMBOL1)
                                  val exchangePairSymbol1: String,
                                  @ColumnInfo(name = ColumnNames.EXCHANGE_PAIR_SYMBOL2)
                                  val exchangePairSymbol2: String,
                                  @ColumnInfo(name = ColumnNames.VOLUME_USD)
                                  val volumeUsd: Double,
                                  @ColumnInfo(name = ColumnNames.PRICE_USD)
                                  val priceUsd: Double,
                                  @ColumnInfo(name = ColumnNames.VOLUME_PERCENT)
                                  val volumePercent: Float,
                                  @ColumnInfo(name = ColumnNames.UPDATED_FLAG)
                                  val updatedFlag: String,
                                  @ColumnInfo(name = ColumnNames.LAST_UPDATED_TIMESTAMP)
                                  val lastUpdatedTimestamp: Long) {
    companion object {
        const val TABLE_NAME = "markets"
    }

    object ColumnNames {
        const val RANK = "rank"
        const val EXCHANGE_NAME = "exchangeName"
        const val EXCHANGE_PAIR_URL = "exchangePairUrl"
        const val COIN_SYMBOL = "coinSymbol"
        const val EXCHANGE_PAIR_SYMBOL1 = "exchangePairSymbol1"
        const val EXCHANGE_PAIR_SYMBOL2 = "exchangePairSymbol2"
        const val VOLUME_USD = "volumeUsd"
        const val PRICE_USD = "priceUsd"
        const val VOLUME_PERCENT = "volumePercent"
        const val UPDATED_FLAG = "updatedFlag"
        const val LAST_UPDATED_TIMESTAMP = "lastUpdatedTimestamp"
    }
}