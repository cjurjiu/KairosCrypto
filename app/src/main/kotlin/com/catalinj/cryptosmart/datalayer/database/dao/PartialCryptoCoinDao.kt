package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.*
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
@Dao
interface PartialCryptoCoinDao {

    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}")
    fun getAll(): List<DbPartialCryptoCoin>

    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}")
    fun monitorRx(): Flowable<List<DbPartialCryptoCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: DbPartialCryptoCoin): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coins: List<DbPartialCryptoCoin>): List<Long>

    @Delete
    fun delete(coin: DbPartialCryptoCoin)

    @Delete
    fun delete(coins: List<DbPartialCryptoCoin>)

}
