package com.catalinj.cryptosmart.network.config

import com.catalinjurjiu.common.Configurator
import okhttp3.OkHttpClient

/**
 * Created by catalinj on 03.02.2018.
 */
class OkHttpConfigurator : Configurator<OkHttpClient> {

    override fun configure(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}