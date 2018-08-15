package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.abstractrequest.ComposableApiRequest
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinListResponse
import io.reactivex.Observable

class BoundedCryptoCoinsRequest(private val startIndex: Int,
                                private val numberOfCoins: Int,
                                private val currencyRepresentation: CurrencyRepresentation,
                                private val coinMarketCapApiService: CoinMarketCapApiService) :
        ComposableApiRequest<CoinMarketCapCryptoCoinListResponse>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinListResponse> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapApiService.fetchCoinsList(start = startIndex,
                limit = numberOfCoins,
                currency = currencyRepresentation.currency)
    }
}