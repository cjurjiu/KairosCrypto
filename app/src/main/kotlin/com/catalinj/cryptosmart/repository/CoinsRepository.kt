package com.catalinj.cryptosmart.repository

import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.datastorage.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import retrofit2.Call

/**
 * Created by catalinj on 28.01.2018.
 */

class CoinsRepository(private val cryptoSmartDb: CryptoSmartDb, private val coinMarketCapService: CoinMarketCapService) {

    fun refreshCoins() {
        val coinsCall: Call<List<CoinMarketCapCryptoCoin>> = coinMarketCapService.fetchCoinsListWithLimit()
        val coins: List<CoinMarketCapCryptoCoin> = coinsCall.execute().body()!!
        val dbCoins: List<DbCryptoCoin> = coins.map { convertRestCoinToDbCoin(it) }
        println("coins size:" + coins.size)
        cryptoSmartDb.getCoinMarketCapCryptoCoinDao().insert(dbCoins)
    }

    fun getCoins(): List<CoinMarketCapCryptoCoin> {
        val dbCoins: List<DbCryptoCoin> = cryptoSmartDb.getCoinMarketCapCryptoCoinDao().getAll()
        return dbCoins.map { convertDbCoinToRestCoin(it) }
    }
}

