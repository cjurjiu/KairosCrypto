package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Immutable object which models a Bookmark as it is stored as a row in the Database.
 *
 * @property id Int which represents the unique ID of the coin. auto-increment, primary key.
 * @property bookmarkedCoinSymbol String representing the coin symbol (e.g. BTC for Bitcoin). maps
 * to an entry from the [DbPartialCryptoCoin.COIN_TABLE_NAME] table.
 * @property dateAdded The time when the coin was bookmarked, as a timestamp since epoch.
 *
 * Created by catalinj on 27.01.2018.
 */
@Entity(tableName = DbBookmark.BOOKMARKS_TABLE_NAME,
        indices = [Index(value = [DbBookmark.ColumnNames.BOOKMARKED_COIN_SYMBOL], unique = true)])
data class DbBookmark(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = DbBookmark.ColumnNames.ID)
        val id: Int,
        @ColumnInfo(name = DbBookmark.ColumnNames.BOOKMARKED_COIN_SYMBOL)
        val bookmarkedCoinSymbol: String,
        @ColumnInfo(name = DbBookmark.ColumnNames.DATE_ADDED)
        val dateAdded: Long) {

    companion object {
        const val BOOKMARKS_TABLE_NAME: String = "bookmarks"
    }

    object ColumnNames {
        const val ID: String = "id"
        const val BOOKMARKED_COIN_SYMBOL: String = "bookmarkedCoinSymbol"
        const val DATE_ADDED: String = "dateAdded"
    }
}