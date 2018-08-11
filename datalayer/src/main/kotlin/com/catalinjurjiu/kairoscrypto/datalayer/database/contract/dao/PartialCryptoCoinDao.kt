package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
interface PartialCryptoCoinDao {

    /**
     * Get a Flowable which monitors the list of known [DbPartialCryptoCoin].
     */
    fun getPartialCryptoCoinsFlowable(): Flowable<List<DbPartialCryptoCoin>>

    fun insert(coin: DbPartialCryptoCoin): Long

    fun insert(coins: List<DbPartialCryptoCoin>): List<Long>
}