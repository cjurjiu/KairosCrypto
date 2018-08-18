package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoinMarketInfo] objects.
 *
 * Created by catalinj on 27.05.2018.
 */
interface MarketsRepository : Repository {

    /**
     * Fetches the markets on which the coin identified by [coinSymbol] is available for trading.
     *
     * Once the coins have been fetched, the observable returned by [getMarketsListObservable] will
     * notify its observers.
     *
     * @param coinSymbol the symbol of the coin (e.g. BTC for Bitcoin)
     * @param webFriendlyName URL friendly identifier of the coin, or website slug, in coinmarketcap
     * terminology. (e.g. "bitcoin-cash" for "Bitcoin Cash")
     * @param errorHandler consumer which will be notified if an error happens. The error handler
     * will be invoked on a background thread.
     **/
    fun fetchMarketsData(coinSymbol: String, webFriendlyName: String, errorHandler: Consumer<Throwable>)

    /**
     * Observable that notifies when the available list of [CryptoCoinMarketInfo]s has been updated,
     * for the coin identified by [coinSymbol].
     *
     * @param coinSymbol the coin symbol of a specific cryptocurrency (e.g. BTC for Bitcoin)
     */
    fun getMarketsListObservable(coinSymbol: String): Observable<List<CryptoCoinMarketInfo>>
}