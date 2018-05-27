package com.catalinj.cryptosmart.di.modules.data

import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapApiService
import com.catalinj.cryptosmart.datalayer.network.config.OkHttpConfigurator
import com.catalinj.cryptosmart.datalayer.network.config.RetrofitConfigurator
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by catalinj on 03.02.2018.
 */
@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideOkHttp(): OkHttpClient {
        return OkHttpConfigurator().configure()
    }

    @Provides
    @ApplicationScope
    @CoinMarketCapApiQualifier
    fun provideCoinMarketCapRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitConfigurator(baseUrl = CoinMarketCapApiService.BASE_URL,
                okHttpClient = okHttpClient,
                converterFactory = GsonConverterFactory.create())
                .configure()
    }

    @Provides
    @ApplicationScope
    @CoinMarketCapHtmlQualifier
    fun provideCoinMarketCapHtmlRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitConfigurator(baseUrl = CoinMarketCapHtmlService.BASE_URL,
                okHttpClient = okHttpClient,
                converterFactory = ScalarsConverterFactory.create())
                .configure()
    }
}