package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.PartialCryptoCoinDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomPartialCryptoCoinDao
import io.reactivex.Flowable

/**
 * Dao which wraps over a [RoomPartialCryptoCoinDao] to respect the contract defined by
 * the [PartialCryptoCoinDao] contract.
 *
 * Created by catalin on 28/01/2018.
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