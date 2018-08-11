package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.request

import android.util.Log
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.abstractrequest.ComposableApiRequest
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails
import io.reactivex.Observable

class CryptoCoinDetailsRequest(private val coinId: String,
                               private val requiredCurrency: CurrencyRepresentation = CurrencyRepresentation.USD,
                               private val coinMarketCapApiService: CoinMarketCapApiService) :
        ComposableApiRequest<CoinMarketCapCryptoCoinDetails>() {

    override fun fetchData(): Observable<CoinMarketCapCryptoCoinDetails> {
        Log.d("RxJ", "fetch data")
        return coinMarketCapApiService.fetchCoinDetails(coinId = coinId,
                currency = requiredCurrency.currency)
    }
}