package com.catalinjurjiu.kairoscrypto.datalayer.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.dao.*
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData

/**
 * Created by catalinj on 27.01.2018.
 */
@Database(version = 1,
        entities = [DbPartialCryptoCoin::class,
            DbPriceData::class,
            DbBookmark::class,
            DbCryptoCoinMarketInfo::class])
abstract class KairosCryptoDb : RoomDatabase() {

    abstract fun getCryptoCoinDao(): CryptoCoinDao

    abstract fun getPlainCryptoCoinDao(): PartialCryptoCoinDao

    abstract fun getCoinMarketCapPriceDataDao(): PriceDataDao

    abstract fun getBookmarksDao(): BookmarksDao

    abstract fun getMarketsInfoDao(): CryptoCoinMarketsDao

    companion object InstanceHolder {

        private const val DATABASE_NAME: String = "KairosCryptoDb"

        private val lock: Any = Any()

        private var INSTANCE: KairosCryptoDb? = null

        fun getInstance(context: Context): KairosCryptoDb {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, KairosCryptoDb::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}