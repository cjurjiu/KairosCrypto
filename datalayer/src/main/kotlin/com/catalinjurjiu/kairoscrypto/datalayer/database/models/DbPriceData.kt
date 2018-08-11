package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by catalin on 08/05/2018.
 */
@Entity(tableName = DbPriceData.PRICE_DATA_TABLE_NAME,
        primaryKeys = [DbPriceData.ColumnNames.COIN_SYMBOL, DbPriceData.ColumnNames.CURRENCY]
//      foreign keys seem to cause race conditions on inserts? comment them for not
//        ,
//        foreignKeys = [
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
        val lastUpdated: Long = -1) {

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