package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.CryptoCoinMarketsDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomCryptoCoinMarketsDao
import io.reactivex.Flowable

internal class CryptoCoinMarketsDaoImpl(private val roomCryptoCoinMarketsDao: RoomCryptoCoinMarketsDao) :
        CryptoCoinMarketsDao {

    override fun getMarketsObservable(coinSymbol: String): Flowable<List<DbCryptoCoinMarketInfo>> {
        return roomCryptoCoinMarketsDao.getMarketsObservable(coinSymbol = coinSymbol)
    }

    override fun insert(cryptoCoinMarketInfo: DbCryptoCoinMarketInfo): Long {
        return roomCryptoCoinMarketsDao.insert(cryptoCoinMarketInfo = cryptoCoinMarketInfo)
    }

    override fun insert(cryptoCoinMarketInfoList: List<DbCryptoCoinMarketInfo>): List<Long> {
        return roomCryptoCoinMarketsDao.insert(cryptoCoinMarketInfoList = cryptoCoinMarketInfoList)
    }
}