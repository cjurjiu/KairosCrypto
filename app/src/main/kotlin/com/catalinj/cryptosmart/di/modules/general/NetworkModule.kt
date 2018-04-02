package com.catalinj.cryptosmart.di.modules.general

import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.network.config.OkHttpConfigurator
import com.catalinj.cryptosmart.network.config.RetrofitConfigurator
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by catalinj on 03.02.2018.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpConfigurator().configure()
    }

    @Provides
    @Singleton
    @CoinMarketCapQualifier
    fun provideCoinMarketCapRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitConfigurator(CoinMarketCapService.BASE_URL, okHttpClient).configure()
    }

    @Provides
    @Singleton
    fun provideCoinMarketCapService(@CoinMarketCapQualifier retrofit: Retrofit): CoinMarketCapService {
        return retrofit.create(CoinMarketCapService::class.java)
    }
}