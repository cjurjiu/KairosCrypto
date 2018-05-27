package com.catalinj.cryptosmart.datalayer.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.catalinj.cryptosmart.datalayer.database.dao.BookmarksDao
import com.catalinj.cryptosmart.datalayer.database.dao.CryptoCoinDao
import com.catalinj.cryptosmart.datalayer.database.dao.PartialCryptoCoinDao
import com.catalinj.cryptosmart.datalayer.database.dao.PriceDataDao
import com.catalinj.cryptosmart.datalayer.database.models.DbBookmark
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbPriceData

/**
 * Created by catalinj on 27.01.2018.
 */
@Database(version = 1, entities = [DbPartialCryptoCoin::class,
    DbPriceData::class,
    DbBookmark::class])
abstract class CryptoSmartDb : RoomDatabase() {

    abstract fun getCryptoCoinDao(): CryptoCoinDao

    abstract fun getPlainCryptoCoinDao(): PartialCryptoCoinDao

    abstract fun getCoinMarketCapPriceDataDao(): PriceDataDao

    abstract fun getBookmarksDao(): BookmarksDao

    companion object InstanceHolder {

        private const val DATABASE_NAME: String = "CryptoSmartDb"

        private val lock: Any = Any()

        private var INSTANCE: CryptoSmartDb? = null

        fun getInstance(context: Context): CryptoSmartDb {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CryptoSmartDb::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}