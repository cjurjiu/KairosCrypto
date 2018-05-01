package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.ApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import io.reactivex.Observable

class BoundedCryptoCoinsRequest(private val startIndex: Int,
                                private val numberOfCoins: Int,
                                private val coinMarketCapService: CoinMarketCapService) :
        ApiRequest<List<CoinMarketCapCryptoCoin>>() {

    override fun fetchData(): Observable<List<CoinMarketCapCryptoCoin>> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapService.rxFetchCoinsListBounded(start = startIndex, limit = numberOfCoins)
    }
}