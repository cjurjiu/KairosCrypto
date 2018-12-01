package com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model.CryptoCompareCoinListByVolume
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model.CryptoCompareMultiCoinFullDataStore
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface CryptoCompareApiService {

    /**
     * Fetches a page of cryptocurrencies from a list of cryptocurrencies sorted by trade volume in
     * the last 24 Hours.
     *
     * @param coinNr The number of coins to return. Max 100
     * @param page The page number
     * @param currency The currency in which the value of the fetched cryptocurrciens is represented.
     * @param signRequest If set to true, the server will sign the requests (by default they're not signed).
     * This is useful for usage in smart contracts
     */
    @GET(value = "$BASE_URL/$ENDPOINT_COINS_BY_TOTAL_VOLUME")
    fun fetchCoinsByTotalVolume(@Query(PARAM_LIMIT) coinNr: Int = 10,
                                @Query(PARAM_PAGE) page: Int = 0,
                                @Query(PARAM_CURRENCY) currency: String = CurrencyRepresentation.BTC.currency,
                                @Query(PARAM_SIGN) signRequest: Boolean = false): Observable<CryptoCompareCoinListByVolume>


    /**
     * Fetches detailed information about a list of cryptocurrencies.
     *
     * @param cryptoCoinSymbolCsv Comma separated cryptocurrency symbols list [Max character length: 300]
     * @param cryptoCoinsCurrencyRepresentationsCsv Comma separated cryptocurrency symbols list to
     * convert into [Max character length: 100]
     * @param appName The name of the application [Max character length: 2000]
     * @param exchangeListCsv The exchange to obtain data from. Defaults to "CCCAGG" (Crypto Compare
     * aggregated data) [Max character length: 30]
     * @param tryConversion If set to false, it will try to get only direct trading values
     * @param signRequest If set to true, the server will sign the requests (by default they're not signed).
     * This is useful for usage in smart contracts
     */
    @GET(value = "$BASE_URL/$ENDPOINT_MULTIPLE_COINS_FULL_DATA")
    fun fetchMultipleCoinsFullData(@Query(PARAM_CRYPTO_TO_FETCH_CSV) cryptoCoinSymbolCsv: String,
                                   @Query(PARAM_CURRENCY_REPRESENTATIONS_CSV) cryptoCoinsCurrencyRepresentationsCsv: String,
                                   @Query(PARAM_APPLICATION_NAME) appName: String = APP_NAME,
                                   @Query(PARAM_EXCHANGE_LIST) exchangeListCsv: String = EXCHANGE_CRYPTO_COMPARE_AGGREGATED_DATA,
                                   @Query(PARAM_TRY_CONVERSION) tryConversion: Boolean = true,
                                   @Query(PARAM_SIGN) signRequest: Boolean = false): Observable<CryptoCompareMultiCoinFullDataStore>

    companion object {
        /**
         * Base url for the Rest API of cointmarketcap.com
         */
        const val BASE_URL: String = "https://min-api.cryptocompare.com"
        //parameters
        //by volume params
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_PAGE = "page"
        private const val PARAM_CURRENCY = "tsym"
        private const val PARAM_SIGN = "sign"
        //multiple coins full data params
        private const val PARAM_TRY_CONVERSION = "tryConversion"
        private const val PARAM_CRYPTO_TO_FETCH_CSV = "fsyms"
        private const val PARAM_CURRENCY_REPRESENTATIONS_CSV = "tsyms"
        private const val PARAM_EXCHANGE_LIST = "e"
        private const val PARAM_APPLICATION_NAME = "extraParams"
        //server groups
        private const val DATA_GROUP: String = "/data"
        //endpoints
        private const val ENDPOINT_COINS_BY_TOTAL_VOLUME = "$DATA_GROUP/top/totalvol"
        private const val ENDPOINT_MULTIPLE_COINS_FULL_DATA = "$DATA_GROUP/pricemultifull"
        //exchanges
        private const val EXCHANGE_CRYPTO_COMPARE_AGGREGATED_DATA = "CCCAGG"
        private const val APP_NAME = "KairosKrypto"
    }
}