package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import io.reactivex.Flowable

/**
 * Created by catalin on 08/05/2018.
 */
interface PriceDataDao {

    /**
     * Get a Flowable which monitors the available price data for a specific cryptocurrency.
     *
     * A cryptocurrency can have its value expressed in several other fiat currencies (or Bitcoin).
     * Use [currency] to select the desired representation of value. If all available value representations
     * are desired, pass [RoomPriceDataDao.ANY_PRICE_DATA][RoomPriceDataDao.ANY_PRICE_DATA]
     *
     * @param coinSymbol the symbol of the targeted cryptocurrency
     * @param currency the fiat currency (or Bitcoin) in which the value of the cryptocurrency will
     * be expressed. See [CurrencyRepresentation][CoinMarketCapApiService.CurrencyRepresentation]
     * for possible values. Use [RoomPriceDataDao.ANY_PRICE_DATA][RoomPriceDataDao.ANY_PRICE_DATA] to be
     * notified of changes to any of the available value representations.
     */
    fun getPriceDataFlowable(coinSymbol: String, currency: String = ANY_PRICE_DATA): Flowable<List<DbPriceData>>

    /**
     * Get the list of available [DbPriceData] items for a specific cryptocurrency.
     *
     * A cryptocurrency can have its value expressed in several other fiat currencies (or Bitcoin).
     * Use [currency] to select the desired representation of value. If all available value representations
     * are desired, pass [RoomPriceDataDao.ANY_PRICE_DATA][RoomPriceDataDao.ANY_PRICE_DATA]
     *
     * @param coinSymbol the symbol of the targeted cryptocurrency
     * @param currency the fiat currency (or Bitcoin) in which the value of the cryptocurrency will
     * be expressed. See [CurrencyRepresentation][CoinMarketCapApiService.CurrencyRepresentation]
     * for possible values. Use [RoomPriceDataDao.ANY_PRICE_DATA][RoomPriceDataDao.ANY_PRICE_DATA] to fetch
     * all the available value representations.
     */
    fun getPriceData(coinSymbol: String, currency: String = ANY_PRICE_DATA): List<DbPriceData>

    fun insert(coinsPriceData: List<DbPriceData>): List<Long>

    fun update(coinsPriceData: List<DbPriceData>): Int

    companion object Constants {
        const val ANY_PRICE_DATA = "%"
    }
}