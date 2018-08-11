package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.PartialCryptoCoinDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomPartialCryptoCoinDao
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
internal class PartialCryptoCoinDaoImpl(private val roomPartialCryptoCoinDao: RoomPartialCryptoCoinDao) :
        PartialCryptoCoinDao {

    override fun getPartialCryptoCoinsFlowable(): Flowable<List<DbPartialCryptoCoin>> {
        return roomPartialCryptoCoinDao.getPartialCryptoCoinsFlowable()
    }

    override fun insert(coin: DbPartialCryptoCoin): Long {
        return roomPartialCryptoCoinDao.insert(coin = coin)
    }

    override fun insert(coins: List<DbPartialCryptoCoin>): List<Long> {
        return roomPartialCryptoCoinDao.insert(coins = coins)
    }
}