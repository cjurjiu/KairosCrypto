package com.catalinjurjiu.kairoscrypto.datalayer.database.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
@Dao
interface RoomPartialCryptoCoinDao {

    /**
     * Get a Flowable which monitors the list of known [DbPartialCryptoCoin].
     */
    @Query("SELECT * FROM ${DbPartialCryptoCoin.COIN_TABLE_NAME}")
    fun getPartialCryptoCoinsFlowable(): Flowable<List<DbPartialCryptoCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: DbPartialCryptoCoin): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coins: List<DbPartialCryptoCoin>): List<Long>
}