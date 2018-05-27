package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.network.ApiRequest
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails
import io.reactivex.Observable

class CryptoCoinDetailsRequest(private val coinId: String,
                               private val requiredCurrency: CurrencyRepresentation = CurrencyRepresentation.USD,
                               private val coinMarketCapApiService: CoinMarketCapApiService) :
        ApiRequest<CoinMarketCapCryptoCoinDetails>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinDetails> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapApiService.fetchCoinDetails(coinId = coinId,
                currency = requiredCurrency.currency)
    }
}