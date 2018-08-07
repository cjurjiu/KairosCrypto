package com.catalinj.cryptosmart.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
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
        val totalSupply: Double,
        @ColumnInfo(name = ColumnNames.MAX_SUPPLY)
        val maxSupply: Double,
        @ColumnInfo(name = ColumnNames.LAST_UPDATED)
        val lastUpdated: Long) {

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
        const val LAST_UPDATED: String = "coin_last_updated"
    }
}