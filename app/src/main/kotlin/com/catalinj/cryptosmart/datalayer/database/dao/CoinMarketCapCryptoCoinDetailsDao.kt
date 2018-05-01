package com.catalinj.cryptosmart.datalayer.database.dao

import android.arch.persistence.room.*
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoinDetails
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
@Dao
interface CoinMarketCapCryptoCoinDetailsDao {

    @Query("SELECT * FROM coins WHERE id LIKE :id")
    fun getCoin(id: String): DbCryptoCoinDetails

    @Query("SELECT * FROM coins WHERE id LIKE :id")
    fun monitorRx(id: String): Flowable<DbCryptoCoinDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: DbCryptoCoinDetails): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coin: DbCryptoCoinDetails)

    @Delete
    fun delete(coin: DbCryptoCoinDetails)

}
