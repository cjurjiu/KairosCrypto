package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Immutable object which models value information (price) for a particular Crypto Coin, in one
 * currency.
 *
 * Has a composite primary key composed of both the Symbol of a coin & the currency in which the
 * price data is stored. This is because one coin can have multiple "Price Data" objects (db rows)
 * associated with it. For instance, the value of a particular cryptocurrency could be represented
 * both relative to the Dollar (USD), and relative to Bitcoin (BTC).
 *
 * @property coinSymbol Primary key. the symbol of the coin whose price data this object stores.
 * @property currency Primary key. currency identifier representing the currency in which the value
 * of designated coin is stored.
 * @property coinServerId Unique ID which can identify the coin owning this price data on the
 * server/backend that provided the price data.
 * @property price The units of value of [currency] for the coin (represented by [coinSymbol]) which
 * owns this value.
 * @property marketCap The units of value of the total market cap of the coin which owns this value,
 * represented in [currency].
 * @property volume24h The units of value traded in the last 24 hours of the coin which owns this value,
 * represented in [currency].
 * @property percentChange1h The change in value over the last hour, in percents.
 * @property percentChange24h The change in value over the last 24 hours, in percents.
 * @property percentChange7d The change in value over the last 7 days, in percents.
 * @property lastUpdated Timestamp since epoch when this price data was last updated.
 *
 * Created by catalinj on 27.01.2018.
 */
@Entity(tableName = DbPriceData.PRICE_DATA_TABLE_NAME,
        primaryKeys = [DbPriceData.ColumnNames.COIN_SYMBOL, DbPriceData.ColumnNames.CURRENCY]
//      foreign keys seem to cause race conditions on inserts? comment them for now
//        ,foreignKeys = [
//            (ForeignKey(entity = DbPartialCryptoCoin::class,
//                    parentColumns = [DbPartialCryptoCoin.ColumnNames.SYMBOL],
//                    childColumns = [DbPriceData.ColumnNames.COIN_SYMBOL],
//                    onUpdate = ForeignKey.CASCADE,
//                    onDelete = ForeignKey.CASCADE))
//        ]
)
data class DbPriceData(
        @ColumnInfo(name = ColumnNames.COIN_SYMBOL)
        val coinSymbol: String,
        @ColumnInfo(name = ColumnNames.CURRENCY)
        val currency: String,
        @ColumnInfo(name = ColumnNames.COIN_SERVER_ID)
        val coinServerId: String,
        @ColumnInfo(name = ColumnNames.PRICE)
        val price: Float,
        @ColumnInfo(name = ColumnNames.MARKET_CAP)
        val marketCap: Double,
        @ColumnInfo(name = ColumnNames.VOLUME_24H)
        val volume24h: Double,
        @ColumnInfo(name = ColumnNames.PERCENT_CHANGE_1H)
        val percentChange1h: Float,
        @ColumnInfo(name = ColumnNames.PERCENT_CHANGE_24H)
        val percentChange24h: Float,
        @ColumnInfo(name = ColumnNames.PERCENT_CHANGE_7D)
        val percentChange7d: Float,
        @ColumnInfo(name = ColumnNames.LAST_UPDATED)
        val lastUpdated: String = "") {

    companion object {
        const val PRICE_DATA_TABLE_NAME = "price_data"
    }

    object ColumnNames {
        const val COIN_SYMBOL: String = "coin_symbol"
        const val CURRENCY: String = "currency"
        const val COIN_SERVER_ID: String = "coin_server_id"
        const val PRICE: String = "price"
        const val MARKET_CAP: String = "market_cap"
        const val VOLUME_24H: String = "volume_24h"
        const val PERCENT_CHANGE_1H: String = "percent_change_1h"
        const val PERCENT_CHANGE_24H: String = "percent_change_24h"
        const val PERCENT_CHANGE_7D: String = "percent_change_7d"
        const val LAST_UPDATED: String = "price_last_updated"
    }
}