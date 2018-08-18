package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoin] objects.
 *
 * Created by catalinj on 28.01.2018.
 */
interface CoinsRepository : Repository {

    /**
     * Fetches fresh coin data from the server.
     *
     * Once the coins have been fetched, the observable returned by [getCoinListObservable] will
     * notify its observers.
     *
     * @param startIndex the index of the first coin in the list
     * @param numberOfCoins the number of coins to fetch
     * @param currencyRepresentation the currency in which the new coin data needs to be fetched
     * @param errorHandler consumer which will be notified if an error happens. The error handler
     * will be invoked on a background thread.
     */
    fun fetchCoins(startIndex: Int,
                   numberOfCoins: Int,
                   currencyRepresentation: CurrencyRepresentation = CurrencyRepresentation.USD,
                   errorHandler: Consumer<Throwable>)

    /**
     * Fetches fresh coin details data from the server.
     *
     * Once the coins have been fetched, the observable returned by [getCoinDetailsObservable] will
     * notify its observers.
     *
     * @param coinId serverId which uniquely identifies the coin
     * @param valueRepresentationsArray array which specifies the currencies in which the coin details
     * will need to be expressed in
     * @param errorHandler consumer which will be notified if an error happens. . The error handler
     * will be invoked on a background thread.
     */
    fun fetchCoinDetails(coinId: String,
                         valueRepresentationsArray: Array<CurrencyRepresentation> = arrayOf(CurrencyRepresentation.USD),
                         errorHandler: Consumer<Throwable>)

    /**
     * Observable which tracks changes to the details of a specific crypto coin.
     *
     * @param coinSymbol the serverId of the coin to track.
     */
    fun getCoinDetailsObservable(coinSymbol: String): Observable<CryptoCoinDetails>

    /**
     * Observable that notifies when the available list of [CryptoCoin]s has been updated.
     */
    fun getCoinListObservable(currencyRepresentation: CurrencyRepresentation): Observable<List<CryptoCoin>>
}