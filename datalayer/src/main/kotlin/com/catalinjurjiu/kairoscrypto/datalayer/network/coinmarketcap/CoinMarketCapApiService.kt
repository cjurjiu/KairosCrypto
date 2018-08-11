package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by catalinj on 27.01.2018.
 */
interface CoinMarketCapApiService {

    /**
     * Fetches the list of coins, starting at the specified index on CoinMarketCap. The currency in
     * which the value of the coins is expressed, can be configured through the second parameter.
     *
     * @param start the index at which the list of returned coins will start, default 0.
     * @param limit how many coins to return. default param with value of 0.
     * @param currency a CurrencyRepresentation instance which represents the currency in which the
     * coins value should be expressed.
     * @return an immutable list of CoinMarketCapCryptoCoinV1 objects.
     */
    @GET(V2_TICKER_ENDPOINT)
    fun rxFetchCoinsListBounded(@Query(START_URL_PARAM)
                                start: Int = 0,
                                @Query(LIMIT_URL_PARAM)
                                limit: Int = 0,
                                @Query(CONVERT_URL_PARAM)
                                currency: String = CurrencyRepresentation.USD.currency)
            : Observable<CoinMarketCapCryptoCoinListResponse>

    //this endpoint returns a list of just one item
    @GET("$V2_TICKER_ENDPOINT/{$PATH_COIN_ID}")
    fun fetchCoinDetails(@Path(PATH_COIN_ID)
                         coinId: String,
                         @Query(CONVERT_URL_PARAM)
                         currency: String = CurrencyRepresentation.USD.currency): Observable<CoinMarketCapCryptoCoinDetails>

    companion object {
        //base url for the Rest API of cointmarketcap.com
        const val BASE_URL: String = "https://api.coinmarketcap.com"
        //ticker endpoint
        const val V2_TICKER_ENDPOINT: String = "/v2/ticker"
        //serverId of the coin which we want to retrieve
        const val PATH_COIN_ID = "serverId"
        //coin  nr limit
        const val LIMIT_URL_PARAM: String = "limit"
        //list coins start
        const val START_URL_PARAM: String = "start"
        //list coins start
        const val CONVERT_URL_PARAM: String = "convert"

    }

}