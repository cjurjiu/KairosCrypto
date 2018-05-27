package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.ApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinListResponse
import io.reactivex.Observable

class BoundedCryptoCoinsRequest(private val startIndex: Int,
                                private val numberOfCoins: Int,
                                private val coinMarketCapApiService: CoinMarketCapApiService) :
        ApiRequest<CoinMarketCapCryptoCoinListResponse>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinListResponse> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapApiService.rxFetchCoinsListBounded(start = startIndex, limit = numberOfCoins)
    }
}