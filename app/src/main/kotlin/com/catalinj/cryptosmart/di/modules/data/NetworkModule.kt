package com.catalinj.cryptosmart.di.modules.data

import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.datalayer.network.config.OkHttpConfigurator
import com.catalinj.cryptosmart.datalayer.network.config.RetrofitConfigurator
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
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
        return RetrofitConfigurator(CoinMarketCapService.BASE_URL, okHttpClient)
                .configure()
    }
}