package com.catalinj.cryptosmart.network.config

import com.catalinjurjiu.common.Configurator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by catalinj on 03.02.2018.
 */
class RetrofitConfigurator(private val baseUrl: String,
                           private val okHttpClient: OkHttpClient) : Configurator<Retrofit> {

    override fun configure(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}