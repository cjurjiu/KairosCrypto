package com.catalinj.cryptosmart.datalayer.database.coindetails

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by catalinj on 28.01.2018.
 */
@Dao
interface CoinMarketCapCryptoCoinDetailsDao {

    @Query("SELECT * FROM ${DbCryptoCoinDetails.COIN_DETAILS_TABLE_NAME} WHERE id LIKE :id")
    fun getCoin(id: String): DbCryptoCoinDetails

    @Query("SELECT * FROM ${DbCryptoCoinDetails.COIN_DETAILS_TABLE_NAME} WHERE id LIKE :id")
    fun monitorRxSingleCoin(id: String): Flowable<DbCryptoCoinDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coin: DbCryptoCoinDetails): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coin: DbCryptoCoinDetails)

    @Delete
    fun delete(coin: DbCryptoCoinDetails)

}
