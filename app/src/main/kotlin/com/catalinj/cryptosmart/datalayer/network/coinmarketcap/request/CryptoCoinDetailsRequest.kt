package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.ApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails
import io.reactivex.Observable

class CryptoCoinDetailsRequest(private val coinId: String,
                               private val coinMarketCapService: CoinMarketCapService) :
        ApiRequest<CoinMarketCapCryptoCoinDetails>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinDetails> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapService.fetchCoinDetails(coinId = coinId)
    }
}