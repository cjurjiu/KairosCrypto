package com.catalinjurjiu.kairoscrypto.datalayer.database.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.*
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