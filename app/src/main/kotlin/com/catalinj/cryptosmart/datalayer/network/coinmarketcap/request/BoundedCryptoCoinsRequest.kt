package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.network.ComposableApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinListResponse
import io.reactivex.Observable

class BoundedCryptoCoinsRequest(private val startIndex: Int,
                                private val numberOfCoins: Int,
                                private val currencyRepresentation: CurrencyRepresentation,
                                private val coinMarketCapApiService: CoinMarketCapApiService) :
        ComposableApiRequest<CoinMarketCapCryptoCoinListResponse>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinListResponse> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapApiService.rxFetchCoinsListBounded(start = startIndex,
                limit = numberOfCoins,
                currency = currencyRepresentation.currency)
    }
}