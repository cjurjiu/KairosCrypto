package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service specialised in fetching the HTML markdown of pages from CoinMarketCap.com, as Strings.
 *
 * Created by catalinj on 27.05.2018.
 */
interface CoinMarketCapHtmlService {

    /**
     * Launches a GET request to CoinMarketCap.com to fetch the HTML page which contains the table
     * with information regarding the markets on which a crypto currency can be traded on.
     *
     * @param cryptoCoinWebsiteSlug the `website slug` as defined by coinmarketcap.com. It's basically
     * the url-friendly name of a coin, e.g. "bitcoin-cash" instead for "Bitcoin Cash"
     *
     * @return a [Single] which will emit the HTML content, as a String.
     */
    @GET("$CURRENCIES_ENDPOINT/{$PATH_COIN_WEBSITE_SLUG}/$MARKETS_PATH_IDENTIFIER")
    fun fetchCoinMarkets(@Path(PATH_COIN_WEBSITE_SLUG) cryptoCoinWebsiteSlug: String): Single<String>

    companion object {
        /**
         * Base url for the cointmarketcap.com html content (website).
         */
        const val BASE_URL: String = "https://coinmarketcap.com"
        //path segments on the URL
        private const val CURRENCIES_ENDPOINT: String = "/currencies"
        private const val MARKETS_PATH_IDENTIFIER: String = "#markets"
        //"website slug" of the coin which we want to retrieve (e.g. "bitcoin-cash" for "Bitcoin Cash")
        private const val PATH_COIN_WEBSITE_SLUG = "websiteSlug"
    }
}