package com.catalinj.cryptosmart.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by catalinj on 27.01.2018.
 */
interface CoinMarketCapService {

    /**
     * Fetches the specified number of coins, starting at index 0.
     *
     * @param limit how many coins to return. default param, with value of 0.
     * @return an immutable list of CoinMarketCapCryptoCoin objects.
     */
    @GET(V1_TICKER_ENDPOINT)
//    +
//            "$LIMIT_URL_PARAM=$LIMIT_URL_PARAM_TOKEN")
    fun fetchCoinsListWithLimit(@Query(LIMIT_URL_PARAM_TOKEN) limit: Int = 0): Call<List<CoinMarketCapCryptoCoin>>

    /**
     * Fetches the list of coins, starting at index 0. The currency in which the value of the coins
     * is expressed, can be configured through the second parameter.
     *
     * @param limit how many coins to return. default param, with value of 0.
     * @param currency a CurrencyRepresentation instance which represents the currency in which the
     * coins value should be expressed.
     * @return an immutable list of CoinMarketCapCryptoCoin objects.
     */
    @GET("$V1_TICKER_ENDPOINT/?" +
            "$LIMIT_URL_PARAM=$LIMIT_URL_PARAM_TOKEN&" +
            "$CONVERT_URL_PARAM=$CONVERT_URL_PARAM_TOKEN")
    fun fetchCoinsListWithLimitConverted(@Path(LIMIT_URL_PARAM_TOKEN) limit: Int = 0,
                                         @Path(CONVERT_URL_PARAM_TOKEN) currency: Int): List<CoinMarketCapCryptoCoin>

    @GET("$V1_TICKER_ENDPOINT/?" +
            "$LIMIT_URL_PARAM=$LIMIT_URL_PARAM_TOKEN&" +
            "$START_URL_PARAM=$START_URL_PARAM_TOKEN")
    fun fetchCoinsListWithStartAndLimit(@Path(START_URL_PARAM_TOKEN) start: Int = 0,
                                        @Path(LIMIT_URL_PARAM_TOKEN) limit: Int = 0): List<CoinMarketCapCryptoCoin>

    @GET("$V1_TICKER_ENDPOINT/?" +
            "$START_URL_PARAM=$START_URL_PARAM_TOKEN&" +
            "$LIMIT_URL_PARAM=$LIMIT_URL_PARAM_TOKEN&" +
            "$CONVERT_URL_PARAM=$CONVERT_URL_PARAM_TOKEN")
    fun fetchCoinsListWithStartAndLimitConverted(@Path(START_URL_PARAM_TOKEN) start: Int = 0,
                                                 @Path(LIMIT_URL_PARAM_TOKEN) limit: Int = 0,
                                                 @Path(CONVERT_URL_PARAM_TOKEN) currency: Int): List<CoinMarketCapCryptoCoin>

    @GET("$V1_TICKER_ENDPOINT/$COIN_ID_TOKEN/")
    fun fetchCoin(@Path(COIN_ID_TOKEN) id: String): CoinMarketCapCryptoCoin

    @GET("$V1_TICKER_ENDPOINT/$COIN_ID_TOKEN/?$CONVERT_URL_PARAM=$CONVERT_URL_PARAM_TOKEN")
    fun fetchCoinConverted(@Path(CONVERT_URL_PARAM_TOKEN) id: String): CoinMarketCapCryptoCoin

    companion object {
        //ticker endpoint
        const val V1_TICKER_ENDPOINT: String = "/v1/ticker"
        //id of the coin which we want to retrieve
        const val COIN_ID_TOKEN = "{id}"
        //coin  nr limit
        const val LIMIT_URL_PARAM: String = "limit"
        const val LIMIT_URL_PARAM_TOKEN: String = "{limit}"
        //list coins start
        const val START_URL_PARAM: String = "start"
        const val START_URL_PARAM_TOKEN: String = "{start}"
        //list coins start
        const val CONVERT_URL_PARAM: String = "convert"
        const val CONVERT_URL_PARAM_TOKEN: String = "{convert}"

        enum class CurrencyRepresentation(val currency: String) {
            AUD("AUD"),
            BRL("BRL"),
            CAD("CAD"),
            CHF("CHF"),
            CLP("CLP"),
            CNY("CNY"),
            CZK("CZK"),
            DKK("DKK"),
            EUR("EUR"),
            GBP("GBP"),
            HKD("HKD"),
            HUF("HUF"),
            IDR("IDR"),
            ILS("ILS"),
            INR("INR"),
            JPY("JPY"),
            KRW("KRW"),
            MXN("MXN"),
            MYR("MYR"),
            NOK("NOK"),
            NZD("NZD"),
            PHP("PHP"),
            PKR("PKR"),
            PLN("PLN"),
            RUB("RUB"),
            SEK("SEK"),
            SGD("SGD"),
            THB("THB"),
            TRY("TRY"),
            TWD("TWD"),
            ZAR("ZAR")
        }
    }
}
