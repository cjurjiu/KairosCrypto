package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import io.reactivex.Flowable

/**
 * Created by catalin on 09/05/2018.
 */
interface CryptoCoinDao {

    /**
     * Gets all available [DbCryptoCoin]s, ordered by rank, ascending.
     */
    fun getCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors the list of known coins.
     */
    fun getCryptoCoinsFlowable(): Flowable<List<DbCryptoCoin>>

    /**
     * Get a Flowable which monitors the list of known coins for which the value in [currency] is
     * also known.
     */
    fun getCryptoCoinsFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    /**
     * Get a Flowable which monitors a specific crypto coin.
     *
     * @param coinSymbol the symbol of the cryptocurrency to track
     */
    fun getSingleCoinFlowable(coinSymbol: String): Flowable<DbCryptoCoin>
}