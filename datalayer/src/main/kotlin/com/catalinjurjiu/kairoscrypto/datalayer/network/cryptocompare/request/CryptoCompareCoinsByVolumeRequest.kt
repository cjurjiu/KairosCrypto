package com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.request

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.abstractrequest.ComposableApiRequest
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.CryptoCompareApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model.CryptoCompareCoinListByVolume
import io.reactivex.Observable

/**
 * Fetches one page of cryptocurrencies, from the list of cryptocurrencies ordered descending w.r.t
 * their trade volume in the last 24 hours. The number of cryptocurrencies in one page is defined by
 * [pageSize]. The index of th fetched page from the complete dataset is defined by [pageNr].
 *
 * @param pageNr the number of the data page to be fetched
 * @param pageSize the number of cryptocurrencies in the fetched page
 * @param currencyRepresentation the currency in which the values of the fetched cryptocurrencies will
 * be expressed
 * @param cryptoCompareApiService service which will be used to fetch the data
 */
class CryptoCompareCoinsByVolumeRequest(private val pageNr: Int,
                                        private val pageSize: Int,
                                        private val currencyRepresentation: CurrencyRepresentation,
                                        private val cryptoCompareApiService: CryptoCompareApiService) :
        ComposableApiRequest<CryptoCompareCoinListByVolume>() {

    override fun fetchData(): Observable<CryptoCompareCoinListByVolume> =
            cryptoCompareApiService.fetchCoinsByTotalVolume(coinNr = pageSize,
                    page = pageNr, currency = currencyRepresentation.currency)
}