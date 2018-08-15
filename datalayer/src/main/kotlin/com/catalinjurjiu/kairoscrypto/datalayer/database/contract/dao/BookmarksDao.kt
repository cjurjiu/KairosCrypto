package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * DAO which provides access to stored [DbBookmark] objects.
 *
 * Created by catalin on 11/05/2018.
 */
interface BookmarksDao {

    /**
     * Gets all available [DbBookmark]s, in no particular order.
     *
     * @return a Single that emits a list of [DbBookmark].
     */
    fun getBookmarks(): Single<List<DbBookmark>>

    /**
     * Gets a [DbBookmark] entry for the coin which has the coin symbol equal to [coinSymbol].
     *
     * @param coinSymbol a String representing the coin symbol (e.g. BTC for Bitcoin) for the desired
     * bookmark.
     * @return a Single that emits the [DbBookmark] which matches the coin symbol specified in the
     * parameter.
     */
    fun getBookmark(coinSymbol: String): Single<DbBookmark>

    /**
     * Get a Flowable which monitors all bookmarks.
     *
     * Whenever a change occurs on one of the known bookmarks, a new list is emitted by the returned
     * Flowable.
     *
     * @return a [Flowable] which monitors all known bookmarks for changes.
     */
    fun getBookmarksFlowable(): Flowable<List<DbBookmark>>

    /**
     * Get a list bookmarked coins as a list of [DbCryptoCoin]s, ordered by date.
     *
     * The coins which were added to bookmarked more recently will be at the front of the list,
     * whereas older bookmarks will be last.
     *
     * @return a list of bookmarked [DbCryptoCoin]s, ordered by the date added to bookmarks.
     */
    fun getBookmarkedCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors all bookmarks.
     *
     * Whenever a change occurs on one of the known bookmarks, a new list is emitted by the returned
     * Flowable.
     *
     * The items emitted here are [DbCryptoCoinSinglePriceData].
     *
     * Use [currency] to specify the currency in which the value of the returned bookmarked coins
     * should be expressed.
     *
     * The coins which were added to bookmarked more recently will be at the front of the list,
     * whereas older bookmarks will be last.
     *
     * @param currency a string obtained from a [CurrencyRepresentation][com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation]
     * which represents the currency in which the value of the bookmark needs to be displayed.
     * @return a [Flowable] which monitors all known bookmarks for changes.
     */
    fun getBookmarksPriceDataFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    /**
     * Inserts a [DbBookmark] in the database, in the bookmarks table.
     *
     * If the equivalent entry already exists(checked by the primary key), the insert is aborted.
     *
     * @param dbBookmark bookmark to be added.
     * @return a long which represents the row id of the inserted row
     */
    fun insert(dbBookmark: DbBookmark): Long

    /**
     * Inserts a list of [DbBookmark]s in the database, in the bookmarks table.
     *
     * If one of the entries already exists(checked by the primary key), the insert is aborted.
     *
     * @param dbBookmarks bookmarks to be added.
     * @return a list of long id's which represent the row ids of the inserted rows
     */
    fun insert(dbBookmarks: List<DbBookmark>): List<Long>

    /**
     * Deletes a [DbBookmark] from the database, from the bookmarks table.
     *
     * If the equivalent entry does not exist, nothing happens.
     *
     * @param dbBookmark bookmark to be deleted.
     * @return a long which represents the row id of the deleted row
     */
    fun delete(dbBookmark: DbBookmark): Int
}