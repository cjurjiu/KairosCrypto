package com.catalinj.cryptosmart.datastorage.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by catalinj on 27.01.2018.
 */
@Entity(tableName = "coins")
data class DbCryptoCoin(@PrimaryKey @ColumnInfo(name = "id") val id: String,
                        @ColumnInfo(name = "name") val name: String,
                        @ColumnInfo(name = "symbol") val symbol: String,
                        @ColumnInfo(name = "rank") val rank: Int,
                        @ColumnInfo(name = "price_usd") val priceUsd: Float,
                        @ColumnInfo(name = "price_btc") val priceBtc: Float,
                        @ColumnInfo(name = "volume_usd_24h") val volumeUsd24h: Double,
                        @ColumnInfo(name = "market_cap_usd") val marketCapUsd: Double,
                        @ColumnInfo(name = "available_supply") val availableSupply: Long,
                        @ColumnInfo(name = "total_supply") val totalSupply: Long,
                        @ColumnInfo(name = "max_supply") val maxSupply: Long,
                        @ColumnInfo(name = "percent_change_1h") val percentChange1h: Float,
                        @ColumnInfo(name = "percent_change_24h") val percentChange24h: Float,
                        @ColumnInfo(name = "percent_change_7d") val percentChange7d: Float,
                        @ColumnInfo(name = "last_updated") val lastUpdated: Long)