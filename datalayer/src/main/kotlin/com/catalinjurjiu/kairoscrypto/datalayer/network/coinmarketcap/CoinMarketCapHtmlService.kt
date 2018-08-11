package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by catalinj on 27.05.2018.
 */
interface CoinMarketCapHtmlService {

    //this endpoint returns a list of just one item
    @GET("$CURRENCIES_ENDPOINT/{$PATH_COIN_WEBSITE_SLUG}/$MARKETS_PATH_IDENTIFIER")
    fun fetchCoinMarkets(@Path(PATH_COIN_WEBSITE_SLUG) websiteSlug: String): Single<String>

    companion object {
        //base url for the cointmarketcap.com html content (website)
        const val BASE_URL: String = "https://coinmarketcap.com"
        //ticker endpoint
        const val CURRENCIES_ENDPOINT: String = "/currencies"
        const val MARKETS_PATH_IDENTIFIER: String = "#markets"
        //serverId of the coin which we want to retrieve
        const val PATH_COIN_WEBSITE_SLUG = "websiteSlug"
    }
}