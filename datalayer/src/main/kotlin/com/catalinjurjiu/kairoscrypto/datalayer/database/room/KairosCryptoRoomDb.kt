package com.catalinjurjiu.kairoscrypto.datalayer.database.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbBookmark
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.KairosCryptoRoomDb.InstanceHolder.getInstance
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.*

/**
 * Room Database definition of the Kairos Crypto application.
 *
 * Provides access to the following DAOs:
 *   * [RoomCryptoCoinDao];
 *   * [RoomPartialCryptoCoinDao];
 *   * [RoomPriceDataDao];
 *   * [RoomBookmarksDao];
 *   * [RoomCryptoCoinMarketsDao].
 *
 *  Obtain the singleton instance through [getInstance].
 *
 * Created by catalinj on 27.01.2018.
 */
@Database(version = 1,
        entities = [DbPartialCryptoCoin::class,
            DbPriceData::class,
            DbBookmark::class,
            DbCryptoCoinMarketInfo::class])
abstract class KairosCryptoRoomDb : RoomDatabase() {

    abstract fun getCryptoCoinDao(): RoomCryptoCoinDao

    abstract fun getPlainCryptoCoinDao(): RoomPartialCryptoCoinDao

    abstract fun getCoinMarketCapPriceDataDao(): RoomPriceDataDao

    abstract fun getBookmarksDao(): RoomBookmarksDao

    abstract fun getMarketsInfoDao(): RoomCryptoCoinMarketsDao

    companion object InstanceHolder {

        private const val DATABASE_NAME: String = "KairosCryptoRoomDb"

        private val lock: Any = Any()

        private var INSTANCE: KairosCryptoRoomDb? = null

        /**
         * Provides a singleton instance of the [KairosCryptoRoomDb].
         *
         * This method is synchronized.
         */
        fun getInstance(context: Context): KairosCryptoRoomDb {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, KairosCryptoRoomDb::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}