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
     * @param errorHandler consumer which will be notified if an error happens
     */
    fun refreshBookmarks(currencyRepresentation: CurrencyRepresentation,
                         errorHandler: Consumer<Throwable>)

    fun refreshBookmarksById(currencyRepresentation: CurrencyRepresentation,
                             bookmarks: List<BookmarksCoin>,
                             errorHandler: Consumer<Throwable>)

    fun isBookmarkLoading(coinSymbol: String): Boolean

    fun getBookmarks(): List<CryptoCoin>

    /**
     * Observable that notifies when the available list of [CryptoCoin]s (representing bookmarks)
     * has been updated.
     */
    fun getBookmarksListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>>

    /**
     * Single returns true if coin symbol represents a bookmark, and false otherwise.
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