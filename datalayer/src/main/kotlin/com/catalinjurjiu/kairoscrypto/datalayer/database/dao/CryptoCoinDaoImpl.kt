package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.CryptoCoinDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomCryptoCoinDao
import io.reactivex.Flowable

/**
 * Created by catalin on 09/05/2018.
 */
internal class CryptoCoinDaoImpl(private val roomCryptoCoinDao: RoomCryptoCoinDao) : CryptoCoinDao {

    override fun getCoins(): List<DbCryptoCoin> {
        return roomCryptoCoinDao.getCoins()
    }

    override fun getCryptoCoinsFlowable(): Flowable<List<DbCryptoCoin>> {
        return roomCryptoCoinDao.getCryptoCoinsFlowable()
    }

    override fun getCryptoCoinsFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>> {
        return roomCryptoCoinDao.getCryptoCoinsFlowable(currency = currency)
    }

    override fun getSingleCoinFlowable(coinSymbol: String): Flowable<DbCryptoCoin> {
        return roomCryptoCoinDao.getSingleCoinFlowable(coinSymbol = coinSymbol)
    }
}