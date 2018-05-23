package com.catalinj.cryptosmart.businesslayer.repository

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoin] objects.
 * Created by catalinj on 28.01.2018.
 */
interface CoinsRepository : Repository {
    /**
     * Updates the known list of coins.
     *
     * Once the coins have been updated, the observable returned by [getCoinDetailsObservable] will
     * notify its observers.
     * @param startIndex the index of the first coin in the list
     * @param numberOfCoins the number of coins to fetch
     * @param errorHandler consumer which will be notified if an error happens
     */
    fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>)

    /**
     * Updates the details of a specific coin market cap coin.
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