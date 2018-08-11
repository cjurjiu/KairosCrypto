package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by catalin on 11/05/2018.
 */
interface BookmarksDao {

    fun getBookmarks(): Single<List<DbBookmark>>

    fun getBookmark(coinSymbol: String): Single<DbBookmark>

    fun getBookmarksFlowable(): Flowable<List<DbBookmark>>

    fun getBookmarkedCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors the list of known coins for which the value in [currency] is
     * also known.
     */
    fun getBookmarksPriceDataFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    fun insert(dbBookmark: DbBookmark): Long

    fun insert(dbBookmark: List<DbBookmark>): List<Long>

    fun delete(dbBookmark: DbBookmark): Int
}