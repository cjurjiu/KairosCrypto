package com.catalinjurjiu.kairoscrypto.di.modules.data

import com.catalinjurjiu.kairoscrypto.datalayer.network.HtmlServiceFactory
import com.catalinjurjiu.kairoscrypto.datalayer.network.RestServiceFactory
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 03.02.2018.
 */
@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideCoinMarketCapRetrofit(): RestServiceFactory {
        return RestServiceFactory
    }

    @Provides
    @ApplicationScope
    fun provideCoinMarketCapHtmlRetrofit(): HtmlServiceFactory {
        return HtmlServiceFactory
    }
}