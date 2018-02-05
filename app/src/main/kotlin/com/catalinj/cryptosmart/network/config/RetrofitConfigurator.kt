package com.catalinj.cryptosmart.network.config

import com.catalinj.cryptosmart.common.config.Configurator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by catalinj on 03.02.2018.
 */
class RetrofitConfigurator(private val okHttpClient: OkHttpClient) : Configurator<Retrofit> {

    override fun configure(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    companion object {
        const val API_URL: String = "https://api.coinmarketcap.com"
    }
}