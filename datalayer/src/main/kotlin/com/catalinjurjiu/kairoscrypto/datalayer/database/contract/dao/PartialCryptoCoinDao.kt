package com.catalinjurjiu.kairoscrypto.datalayer.database.contract.dao

import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import io.reactivex.Flowable

/**
 * DAO which provides access to stored [DbPartialCryptoCoin] objects.
 *
 * Created by catalinj on 28.01.2018.
 */
interface PartialCryptoCoinDao {

    /**
     * Get a Flowable which monitors the list of known [DbPartialCryptoCoin].
     *
     * Whenever a change occurs on one of the known coins, a new list is emitted by the returned
     * Flowable.
     *
     * @return a [Flowable] which monitors all known partial coins for changes.
     */
    fun getPartialCryptoCoinsFlowable(): Flowable<List<DbPartialCryptoCoin>>

    /**
     * Inserts a [DbPartialCryptoCoin] in the database, in the db partial crypto coin table.
     *
     * If one of the entries already exists(checked by the primary key), the insert already existing
     * item is overwritten by the new one.
     *
     * @param coin partial crypto coin object to be added.
     * @return a long which represents the row id of the inserted row
     */
    fun insert(coin: DbPartialCryptoCoin): Long

    /**
     * Inserts a list of [DbPartialCryptoCoin]s in the database, in the db partial crypto coin table.
     *
     * If one of the entries already exists(checked by the primary key), the insert already existing
     * item is overwritten by the new one.
     *
     * @param coins partial crypto coin objects to be added.
     * @return a list of long id's which represent the row ids of the inserted rows
     */
    fun insert(coins: List<DbPartialCryptoCoin>): List<Long>
}