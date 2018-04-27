package com.catalinj.cryptosmart.datalayer.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.catalinj.cryptosmart.datalayer.database.dao.CoinMarketCapCryptoCoinDao
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoin

/**
 * Created by catalinj on 27.01.2018.
 */
@Database(entities = [(DbCryptoCoin::class)], version = 1)
abstract class CryptoSmartDb : RoomDatabase() {

    abstract fun getCoinMarketCapCryptoCoinDao(): CoinMarketCapCryptoCoinDao

    companion object InstanceHolder {

        private const val DATABASE_NAME: String = "CryptoSmartDb"

        private val lock: Any = Any()

        private var INSTANCE: CryptoSmartDb? = null

        fun getInstance(context: Context): CryptoSmartDb {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CryptoSmartDb::class.java, DATABASE_NAME).build()
                }
            }
            return INSTANCE!!
        }
    }
}