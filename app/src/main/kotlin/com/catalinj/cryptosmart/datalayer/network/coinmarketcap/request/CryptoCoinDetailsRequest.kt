package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.ApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import io.reactivex.Observable

class CryptoCoinDetailsRequest(private val coinId: String,
                               private val coinMarketCapService: CoinMarketCapService) :
        ApiRequest<CoinMarketCapCryptoCoin>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoin> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapService.fetchCoinDetails(id = coinId)
    }
}