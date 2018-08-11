package com.catalinjurjiu.kairoscrypto.datalayer.database.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao.PriceDataDao
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao.RoomPriceDataDao
import io.reactivex.Flowable

/**
 * Created by catalin on 08/05/2018.
 */
internal class PriceDataDaoImpl(private val roomPriceDataDao: RoomPriceDataDao) : PriceDataDao {

    override fun getPriceDataFlowable(coinSymbol: String, currency: String): Flowable<List<DbPriceData>> {
        return roomPriceDataDao.getPriceDataFlowable(coinSymbol = coinSymbol, currency = currency)
    }

    override fun getPriceData(coinSymbol: String, currency: String): List<DbPriceData> {
        return roomPriceDataDao.getPriceData(coinSymbol = coinSymbol, currency = currency)
    }

    override fun insert(coinsPriceData: List<DbPriceData>): List<Long> {
        return roomPriceDataDao.insert(coinsPriceData = coinsPriceData)
    }

    override fun update(coinsPriceData: List<DbPriceData>): Int {
        return roomPriceDataDao.update(coinsPriceData = coinsPriceData)
    }
}