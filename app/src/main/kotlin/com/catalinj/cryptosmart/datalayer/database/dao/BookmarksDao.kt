package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.catalinj.cryptosmart.datalayer.database.models.*
import io.reactivex.Flowable

/**
 * Created by catalin on 11/05/2018.
 */
@Dao
interface BookmarksDao {

    @Query("SELECT * FROM ${DbBookmark.BOOKMARKS_TABLE_NAME}")
    fun getBookmarks(): List<DbBookmark>

    @Query("SELECT * FROM ${DbBookmark.BOOKMARKS_TABLE_NAME}")
    fun getBookmarksFlowable(): Flowable<List<DbBookmark>>

    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbBookmark.BOOKMARKS_TABLE_NAME}" +
            " ON ${DbBookmark.BOOKMARKS_TABLE_NAME}.${DbBookmark.ColumnNames.BOOKMARKED_COIN_SYMBOL} = " +
            "${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL}" +
            " ORDER BY ${DbBookmark.BOOKMARKS_TABLE_NAME}.${DbBookmark.ColumnNames.DATE_ADDED} DESC")
    fun getBookmarkedCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors the list of known coins for which the value in [currency] is
     * also known.
     */
    @Transaction
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}" +
            " INNER JOIN ${DbBookmark.BOOKMARKS_TABLE_NAME}" +
            " ON ${DbBookmark.BOOKMARKS_TABLE_NAME}.${DbBookmark.ColumnNames.BOOKMARKED_COIN_SYMBOL} = " +
            "${DbPartialCryptoCoin.COIN_TABLE_NAME}.${DbPartialCryptoCoin.ColumnNames.SYMBOL}" +
            " INNER JOIN ${DbPriceData.PRICE_DATA_TABLE_NAME}" +
            " ON ${DbPartialCryptoCoin.ColumnNames.SYMBOL} = " +
            "${DbPriceData.PRICE_DATA_TABLE_NAME}.${DbPriceData.ColumnNames.COIN_SYMBOL}" +
            " WHERE ${DbPriceData.ColumnNames.CURRENCY} = :currency" +
            " ORDER BY ${DbBookmark.ColumnNames.DATE_ADDED} DESC")
    fun getBookmarksPriceDataFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    @Insert
    fun insert(dbBookmark: DbBookmark)

    @Insert
    fun insert(dbBookmark: List<DbBookmark>): List<Long>
}