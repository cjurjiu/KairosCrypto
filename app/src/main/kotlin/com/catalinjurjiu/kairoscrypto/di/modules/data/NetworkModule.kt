package com.catalinjurjiu.kairoscrypto.di.modules.data

import android.content.Context
import com.catalinjurjiu.common.ActiveActivityProvider
import com.catalinjurjiu.kairoscrypto.datalayer.network.HtmlServiceFactory
import com.catalinjurjiu.kairoscrypto.datalayer.network.RestServiceFactory
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.ApplicationContext
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ApplicationScope
import com.example.cryptodrawablesprovider.GithubCryptoIconHelper
import com.example.cryptodrawablesprovider.ImageHelper
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

    @Provides
    @ApplicationScope
    fun provideImageHelper(@ApplicationContext context: Context,
                           activeActivityProvider: ActiveActivityProvider): ImageHelper<String> {
        return GithubCryptoIconHelper(context = context,
                activeActivityProvider = activeActivityProvider)
    }
}