package com.catalinj.cryptosmart.datalayer.network.coinmarketcap

import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
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
    fun fetchCoinsListWithLimit(@Query(LIMIT_URL_PARAM) limit: Int = 0): Call<List<CoinMarketCapCryptoCoin>>

    @GET(V1_TICKER_ENDPOINT)
    fun rxFetchCoinsListWithLimit(@Query(LIMIT_URL_PARAM) limit: Int = 100): Observable<List<CoinMarketCapCryptoCoin>>

    /**
     * Fetches the list of coins, starting at the specified index on CoinMarketCap. The currency in
     * which the value of the coins is expressed, can be configured through the second parameter.
     *
     * @param start the index at which the list of returned coins will start, default 0.
     * @param limit how many coins to return. default param with value of 0.
     * @param currency a CurrencyRepresentation instance which represents the currency in which the
     * coins value should be expressed.
     * @return an immutable list of CoinMarketCapCryptoCoin objects.
     */
    @GET(V1_TICKER_ENDPOINT)
    fun rxFetchCoinsListBounded(@Query(START_URL_PARAM)
                                start: Int = 0,
                                @Query(LIMIT_URL_PARAM)
                                limit: Int = 0,
                                @Query(CONVERT_URL_PARAM)
                                currency: String = CurrencyRepresentation.USD.currency):
            Observable<List<CoinMarketCapCryptoCoin>>

//    @GET("${V1_TICKER_ENDPOINT}/${COIN_ID_TOKEN}/")
//    fun fetchCoin(@Path(COIN_ID_TOKEN) id: String): CoinMarketCapCryptoCoin
//
//    @GET("${V1_TICKER_ENDPOINT}/${COIN_ID_TOKEN}/?${CONVERT_URL_PARAM}=${CONVERT_URL_PARAM_TOKEN}")
//    fun fetchCoinConverted(@Path(CONVERT_URL_PARAM_TOKEN) id: String): CoinMarketCapCryptoCoin

    companion object {
        //base url for the Rest API of cointmarketcap.com
        const val BASE_URL: String = "https://api.coinmarketcap.com"
        //ticker endpoint
        const val V1_TICKER_ENDPOINT: String = "/v1/ticker"
        //id of the coin which we want to retrieve
        const val COIN_ID_NAME = "id"
        //coin  nr limit
        const val LIMIT_URL_PARAM: String = "limit"
        //list coins start
        const val START_URL_PARAM: String = "start"
        //list coins start
        const val CONVERT_URL_PARAM: String = "convert"

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
            USD("USD"),
            TRY("TRY"),
            TWD("TWD"),
            ZAR("ZAR")
        }
    }
}
