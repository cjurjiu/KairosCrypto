package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Immutable object which models a "partial" Crypto Coin as it is stored as a row in the Database.
 *
 * The object here is named "partial" because it stores no data about the value of the particular coin
 * it represents (i.e. the stored data contains nothing related to the price of the coin).
 *
 * @property rank Int primary key. the rank of the coin among other crypto currencies, based on the
 * coin's market cap. unique by design.
 * @property serverId An unique Id which can identify the coin on a specific service/server.
 * @property name The colloquial name of the coin (e.g. "Bitcoin", or "Bitcoin Cash").
 * @property symbol The trading symbol of the coin (e.g. "BTC" for Bitcoin).
 * @property websiteSlug The url-friendly identifier of the coin. typically it's based on the name.
 * An example would be "bitcoin-cash" for "Bitcoin Cash".
 * @property circulatingSupply Coin number of units that are currently in public hands and circulating
 * (available for trade) in the market.
 * @property totalSupply Coin total number of units that are currently in existence however not all
 * are circulating.
 * @property maxSupply Coin maximum number of units that will ever exist.
 * @property lastUpdated Timestamp since epoch when the last update of this row has occurred.
 * Created by catalinj on 27.01.2018.
 */
@Entity(tableName = DbPartialCryptoCoin.COIN_TABLE_NAME,
        indices = [
            Index(value = [DbPartialCryptoCoin.ColumnNames.SYMBOL], unique = true),
            Index(value = [DbPartialCryptoCoin.ColumnNames.RANK], unique = true)
        ]
)
data class DbPartialCryptoCoin(
        @PrimaryKey
        @ColumnInfo(name = ColumnNames.RANK)
        val rank: Int,
        @ColumnInfo(name = ColumnNames.SERVER_ID)
        val serverId: String,
        @ColumnInfo(name = ColumnNames.NAME)
        val name: String,
        @ColumnInfo(name = ColumnNames.SYMBOL)
        val symbol: String,
        @ColumnInfo(name = ColumnNames.WEBSITE_SLUG)
        val websiteSlug: String,
        @ColumnInfo(name = ColumnNames.CIRCULATING_SUPPLY)
        val circulatingSupply: Double,
        @ColumnInfo(name = ColumnNames.TOTAL_SUPPLY)
        val totalSupply: Double,
        @ColumnInfo(name = ColumnNames.MAX_SUPPLY)
        val maxSupply: Double,
        @ColumnInfo(name = ColumnNames.LAST_UPDATED)
        val lastUpdated: String) {

    companion object {
        const val COIN_TABLE_NAME: String = "coins"
    }

    object ColumnNames {
        const val SERVER_ID: String = "server_id"
        const val RANK: String = "rank"
        const val NAME: String = "name"
        const val SYMBOL: String = "symbol"
        const val WEBSITE_SLUG: String = "website_slug"
        const val CIRCULATING_SUPPLY: String = "circulating_supply"
        const val MAX_SUPPLY: String = "max_supply"
        const val TOTAL_SUPPLY: String = "total_supply"
        const val LAST_UPDATED: String = "coin_last_updated"
    }
}