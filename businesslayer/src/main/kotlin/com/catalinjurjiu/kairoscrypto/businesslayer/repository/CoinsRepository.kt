package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoin] objects.
 *
 * Created by catalinj on 28.01.2018.
 */
interface CoinsRepository : Repository {

    /**
     * Returns the number of coins contained in one data page returned by this repository.
     */
    val dataPageSize: Int

    /**
     * Fetches fresh coin data from the server.
     *
     * The returned data represents one or more pages of coin data. Several pages worth of data can
     * be fetched at once. The number of coins stored in one data page is stored in [dataPageSize].
     * Fewer coins might be returned if the end of the data set is reached.
     *
     * Once the coins have been fetched, the observable returned by [getCoinListObservable] will
     * notify its observers.
     *
     * @param pageIndex the index of the first data page (in the data set) to fetch. 0-indexed.
     * @param numberOfPages the number of data pages to fetch. Defaults to 1.
     * @param currencyRepresentation the currency in which the fetched coins will be represented
     * @return a [Single] which receives the most recent data page which was loaded in case of
     * success. This is usually `pageIndex + nrOfPages - 1`. Receives a throwable in case of a failure.
     */
    fun fetchCoins(pageIndex: Int,
                   numberOfPages: Int = 1,
                   currencyRepresentation: CurrencyRepresentation = CurrencyRepresentation.USD): Single<Int>

    /**
     * Fetches fresh coin details data from the server.
     *
     * Once the coins have been fetched, the observable returned by [getCoinDetailsObservable] will
     * notify its observers.
     *
     * @param coinId serverId which uniquely identifies the coin
     * @param valueRepresentationsArray array which specifies the currencies in which the coin details
     * will need to be expressed in
     * @return a [Single] which receives the [coinId] for which the details were fetched in case of
     * success, or the throwable which occurred in case of a failure
     */
    fun fetchCoinDetails(coinId: String,
                         valueRepresentationsArray: Array<CurrencyRepresentation> = arrayOf(CurrencyRepresentation.USD))
            : Single<String>

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

    companion object {
        /**
         * Default number of coins returned in one page of data.
         */
        const val DEFAULT_DATA_PAGE_SIZE = 100
    }
}