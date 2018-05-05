package com.catalinj.cryptosmart.datalayer.database.coins

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */

@Dao
interface CoinMarketCapCryptoCoinDao {

    @Query("SELECT * FROM ${DbCryptoCoin.COIN_TABLE_NAME}")
    fun getAll(): List<DbCryptoCoin>

    @Query("SELECT * FROM ${DbCryptoCoin.COIN_TABLE_NAME}")
    fun monitorRx(): Flowable<List<DbCryptoCoin>>

    @Query("SELECT * FROM ${DbCryptoCoin.COIN_TABLE_NAME} WHERE id LIKE :id")
    fun getCoin(id: String): List<DbCryptoCoin>

    @Query("SELECT * FROM ${DbCryptoCoin.COIN_TABLE_NAME} WHERE id IN (:coins)")
    fun filter(coins: List<String>): List<DbCryptoCoin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: DbCryptoCoin): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coins: List<DbCryptoCoin>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coin: DbCryptoCoin)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coins: List<DbCryptoCoin>)

    @Delete
    fun delete(coin: DbCryptoCoin)

    @Delete
    fun delete(coins: List<DbCryptoCoin>)

}
