package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.BookmarksDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomBookmarksDao
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by catalin on 11/05/2018.
 */
internal class BookmarksDaoImpl(private val roomBookmarksDao: RoomBookmarksDao) : BookmarksDao {

    override fun getBookmarks(): Single<List<DbBookmark>> {
        return roomBookmarksDao.getBookmarks()
    }

    override fun getBookmark(coinSymbol: String): Single<DbBookmark> {
        return roomBookmarksDao.getBookmark(coinSymbol = coinSymbol)
    }

    override fun getBookmarksFlowable(): Flowable<List<DbBookmark>> {
        return roomBookmarksDao.getBookmarksFlowable()
    }

    override fun getBookmarkedCoins(): List<DbCryptoCoin> {
        return roomBookmarksDao.getBookmarkedCoins()
    }

    override fun getBookmarksPriceDataFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>> {
        return roomBookmarksDao.getBookmarksPriceDataFlowable(currency = currency)
    }

    override fun insert(dbBookmark: DbBookmark): Long {
        return roomBookmarksDao.insert(dbBookmark = dbBookmark)
    }

    override fun insert(dbBookmark: List<DbBookmark>): List<Long> {
        return roomBookmarksDao.insert(dbBookmark = dbBookmark)
    }

    override fun delete(dbBookmark: DbBookmark): Int {
        return roomBookmarksDao.delete(dbBookmark = dbBookmark)
    }
}