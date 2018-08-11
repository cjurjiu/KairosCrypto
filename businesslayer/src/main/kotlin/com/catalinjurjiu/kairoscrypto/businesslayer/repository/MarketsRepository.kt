package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by catalinj on 27.05.2018.
 */
interface MarketsRepository : Repository {

    /**
     * Updates the markets on which the given [cryptoCoin] is available for trading.
     */
    fun updateMarketsData(coinSymbol: String, webFriendlyName: String, errorHandler: Consumer<Throwable>)

    /**
     * Observable that notifies when the available list of [CryptoCoin]s has been updated.
     */
    fun getMarketsListObservable(coinSymbol: String): Observable<List<CryptoCoinMarketInfo>>
}