package com.catalinjurjiu.kairoscrypto.datalayer.network

import com.catalinjurjiu.kairoscrypto.datalayer.BuildConfig
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RestServiceFactory {

    private val retrofit2: Retrofit

    init {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request: Request = original.newBuilder()
                    .header(CoinMarketCapApiService.API_KEY_HEADER, BuildConfig.COINMARKETCAP_API_KEY)
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }

        retrofit2 = RetrofitFactory(baseUrl = CoinMarketCapApiService.BASE_URL,
                okHttpClient = httpClient.build(),
                converterFactory = GsonConverterFactory.create())
                .build()
    }

    fun getCoinsRestServiceApi(): CoinMarketCapApiService {
        return retrofit2.create(CoinMarketCapApiService::class.java)
    }
}

object HtmlServiceFactory {

    private val retrofit: Retrofit = RetrofitFactory(baseUrl = CoinMarketCapHtmlService.BASE_URL,
            okHttpClient = OkHttpClient.Builder().build(),
            converterFactory = ScalarsConverterFactory.create())
            .build()

    fun getMarketsHtmlServiceApi(): CoinMarketCapHtmlService {
        return retrofit.create(CoinMarketCapHtmlService::class.java)
    }
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