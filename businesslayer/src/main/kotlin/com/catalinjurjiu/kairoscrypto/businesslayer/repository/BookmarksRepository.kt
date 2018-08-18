package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoin] objects, which
 * were bookmarked by the user.
 *
 * Created by catalinj on 28.01.2018.
 */
interface BookmarksRepository : Repository {

    /**
     * Updates the known list of bookmarked coins.
     *
     * Once the coins have been updated, the observable returned by [getBookmarksListObservable] will
     * notify its observers.
     *
     * @param currencyRepresentation currency in which the refreshed bookmarks need to be expressed
     * @param errorHandler consumer which will be notified if an error happens. The error handler
     * will be invoked on a background thread.
     */
    fun refreshBookmarks(currencyRepresentation: CurrencyRepresentation,
                         errorHandler: Consumer<Throwable>)

    /**
     * Performs a refresh on the bookmarks specified by [bookmarks].
     *
     * Whenever a refresh finishes for one of the coins, the observable returned by [getBookmarksListObservable]
     * will notify its observers with an updated list of coins.
     *
     * @param currencyRepresentation currency in which the refreshed bookmarks need to be expressed
     * @param bookmarks the list of bookmarks that need to be refreshed.
     * @param errorHandler consumer which will be notified if an error happens. The error handler
     * will be invoked on a background thread.
     */
    fun refreshBookmarksById(currencyRepresentation: CurrencyRepresentation,
                             bookmarks: List<BookmarksCoin>,
                             errorHandler: Consumer<Throwable>)

    /**
     * Informs whether data about a particular bookmark coin is currently being loaded or not.
     *
     * @param coinSymbol the symbol of the coin for which the loading state is checked.
     * @return `true` if the coin is currently loading, `false` otherwise.
     */
    fun isBookmarkLoading(coinSymbol: String): Boolean

    /**
     * Returns the list of known bookmarks.
     *
     * This method is blocking.
     */
    fun getBookmarks(): List<CryptoCoin>

    /**
     * Observable that notifies whenever the list of Bookmarked [CryptoCoin]s is updated.
     *
     * @param currencyRepresentation the currency in which the bookmarks need to be represented
     */
    fun getBookmarksListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>>

    /**
     * Single returns true if coin symbol represents a bookmark, and false otherwise.
     *
     * @param coinSymbol the symbol of the coin to check
     */
    fun isBookmark(coinSymbol: String): Single<Boolean>

    /**
     * Single returns true if add was successful, false otherwise
     */
    fun addBookmark(coinSymbol: String): Single<Boolean>

    /**
     * Single returns true if delete was successful, false otherwise
     */
    fun deleteBookmark(coinSymbol: String): Single<Boolean>
}