package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import io.reactivex.Flowable

/**
 * DAO which provides access to stored [DbCryptoCoin] objects.
 *
 * Created by catalin on 09/05/2018.
 */
interface CryptoCoinDao {

    /**
     * Gets all available [DbCryptoCoin]s, ordered by rank, ascending.
     *
     * @return a list of [DbCryptoCoin], ordered ascending by rank.
     */
    fun getCoins(): List<DbCryptoCoin>

    /**
     * Get a Flowable which monitors all known coins.
     *
     * Whenever a change occurs on one of the known coins, a new list is emitted by the returned
     * Flowable.
     *
     * @return a [Flowable] which monitors all known coins for changes.
     */
    fun getCryptoCoinsFlowable(): Flowable<List<DbCryptoCoin>>

    /**
     * Get a Flowable which monitors all known coins which have their currency represented in [currency].
     *
     * Whenever a change occurs on one of the known coins, a new list is emitted by the returned
     * Flowable.
     *
     * @param currency a String obtained from a [CurrencyRepresentation][com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation]
     * representing the currency in which the monitored coins need to be represented.
     * @return a [Flowable] which monitors all known coins with values as [currency] for changes.
     */
    fun getCryptoCoinsFlowable(currency: String): Flowable<List<DbCryptoCoinSinglePriceData>>

    /**
     * Get a Flowable which monitors a specific crypto coin.
     *
     * @param coinSymbol the symbol of the cryptocurrency to track
     */
    fun getSingleCoinFlowable(coinSymbol: String): Flowable<DbCryptoCoin>
}