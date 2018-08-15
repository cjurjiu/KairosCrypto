package com.catalinjurjiu.kairoscrypto.datalayer.database

import android.content.Context
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.*
import com.catalinjurjiu.kairoscrypto.datalayer.database.dao.*
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.KairosCryptoRoomDb

/**
 * Wraps over a [KairosCryptoRoomDb] in order to respect the contract defined by
 * [KairosCryptoDb].
 *
 * Get the singleton instance via [KairosCryptoDbFactory.getDatabase].
 */
internal class KairosCryptoDbImpl(private val roomKairosCryptoDb: KairosCryptoRoomDb) : KairosCryptoDb {

    override fun getCryptoCoinDao(): CryptoCoinDao {
        val roomDao = roomKairosCryptoDb.getCryptoCoinDao()
        return CryptoCoinDaoImpl(roomCryptoCoinDao = roomDao)
    }

    override fun getPlainCryptoCoinDao(): PartialCryptoCoinDao {
        val roomDao = roomKairosCryptoDb.getPlainCryptoCoinDao()
        return PartialCryptoCoinDaoImpl(roomPartialCryptoCoinDao = roomDao)
    }

    override fun getCoinMarketCapPriceDataDao(): PriceDataDao {
        val roomDao = roomKairosCryptoDb.getCoinMarketCapPriceDataDao()
        return PriceDataDaoImpl(roomPriceDataDao = roomDao)
    }

    override fun getBookmarksDao(): BookmarksDao {
        val roomDao = roomKairosCryptoDb.getBookmarksDao()
        return BookmarksDaoImpl(roomBookmarksDao = roomDao)
    }

    override fun getMarketsInfoDao(): CryptoCoinMarketsDao {
        val roomDao = roomKairosCryptoDb.getMarketsInfoDao()
        return CryptoCoinMarketsDaoImpl(roomCryptoCoinMarketsDao = roomDao)
    }
}

object KairosCryptoDbFactory {
    private lateinit var instance: KairosCryptoDbImpl

    fun getDatabase(context: Context): KairosCryptoDb {
        synchronized(this) {
            if (!::instance.isInitialized) {
                instance = KairosCryptoDbImpl(KairosCryptoRoomDb.getInstance(context))
            }
        }
        return instance
    }
}