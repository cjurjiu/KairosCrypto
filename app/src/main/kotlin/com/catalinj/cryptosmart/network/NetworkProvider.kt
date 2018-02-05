package com.catalinj.cryptosmart.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by catalinj on 03.02.2018.
 */
class NetworkProvider {
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun <T> getService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

    companion object {
        const val API_URL: String = "https://api.coinmarketcap.com"
    }
}