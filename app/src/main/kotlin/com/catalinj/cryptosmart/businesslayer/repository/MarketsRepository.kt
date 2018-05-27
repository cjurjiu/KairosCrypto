package com.catalinj.cryptosmart.businesslayer.repository

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
import io.reactivex.Observable

/**
 * Created by catalinj on 27.05.2018.
 */
interface MarketsRepository : Repository {

    /**
     * Updates the markets on which the given [cryptoCoin] is available for tradingl
     */
    fun updateMarketsData(websiteSlug: String)

    /**
     * Observable that notifies when the available list of [CryptoCoin]s has been updated.
     */
    fun getMarketsListObservable(coinSymbol: String): Observable<List<CryptoCoinMarketInfo>>
}