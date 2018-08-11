package com.catalinjurjiu.kairoscrypto.datalayer.network

import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RestServiceFactory {

    private val retrofit: Retrofit = RetrofitFactory(baseUrl = CoinMarketCapApiService.BASE_URL,
            okHttpClient = OkHttpFactory.okHttpClient,
            converterFactory = GsonConverterFactory.create())
            .build()

    fun getCoinsRestServiceApi(): CoinMarketCapApiService {
        return retrofit.create(CoinMarketCapApiService::class.java)
    }
}

object HtmlServiceFactory {

    private val retrofit: Retrofit = RetrofitFactory(baseUrl = CoinMarketCapHtmlService.BASE_URL,
            okHttpClient = OkHttpFactory.okHttpClient,
            converterFactory = ScalarsConverterFactory.create())
            .build()

    fun getMarketsHtmlServiceApi(): CoinMarketCapHtmlService {
        return retrofit.create(CoinMarketCapHtmlService::class.java)
    }
}

object OkHttpFactory {
    val okHttpClient = OkHttpClient.Builder().build()
}

private class RetrofitFactory(private val baseUrl: String,
                              private val okHttpClient: OkHttpClient,
                              private val converterFactory: Converter.Factory) {

    fun build(): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

}