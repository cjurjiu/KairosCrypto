package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Immutable object which models a market on which a particular Crypto Coin can be traded on (e.g.
 * Binance for Bitcoin).
 *
 * Has a composite primary key composed of the coin symbol & the rank of a particular exchange,
 * in terms of volume, relative to other exchanges.
 *
 * Describes the volume of trading in terms of a trading pair (e.g. BTC/ETH for Bitcoin).
 *
 * @property rank Primary key. the rank of this exchange, in volume of units of the owning coin,
 * relative to other exchanges.
 * @property exchangeName The name of the exchange.
 * @property exchangePairUrl URL which points to the URL specific to the current exchange which displays
 * the current trading pair.
 * @property coinSymbol The symbol of the coin for which the information is stored in this object/row.
 * @property exchangePairSymbol1 The Symbol of the first coin in the trading pair.
 * @property exchangePairSymbol2 The Symbol of the second coin in the trading pair.
 * @property volumeUsd The trading volume of the coin pair stored in this object in USD.
 * @property priceUsd The price in USD of the coin pair stored in this object.
 * @property volumePercent The percent which indicates the percent of the current exchange, relative
 * to the total number of transactions executed on the market for this trading pair.
 * @property updatedFlag Whether the information stored in this object has been updated recently or
 * not.
 * @property lastUpdatedTimestamp Timestamp since epoch when the data in this row has been last fetched
 * from the backend.
 *
 * Created by catalinj on 27.05.2018.
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