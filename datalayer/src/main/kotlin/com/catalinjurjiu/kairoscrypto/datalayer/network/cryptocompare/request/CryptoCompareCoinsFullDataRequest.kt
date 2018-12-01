package com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.request

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.network.abstractrequest.ComposableApiRequest
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.CryptoCompareApiService
import com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model.CryptoCompareMultiCoinFullDataStore
import io.reactivex.Observable

/**
 * Fetches `full data` for the cryptocurrencies specified in [cryptoCurrenciesSymbolList], in
 * the specified [currencyRepresentation], using [cryptoCompareApiService].
 *
 * @param cryptoCurrenciesSymbolList list of symbols for the cryptocurrencies for which data will be
 * fetched. **Note**: Maximum 60 entries, otherwise server will reject the request.
 * @param currencyRepresentation the currency in which the values of the fetched cryptocurrencies will
 * be expressed
 * @param cryptoCompareApiService service which will be used to fetch the data
 */
class CryptoCompareCoinsFullDataRequest(private val cryptoCurrenciesSymbolList: List<String>,
                                        private val currencyRepresentation: CurrencyRepresentation,
                                        private val cryptoCompareApiService: CryptoCompareApiService) :
        ComposableApiRequest<CryptoCompareMultiCoinFullDataStore>() {

    override fun fetchData(): Observable<CryptoCompareMultiCoinFullDataStore> {
        val cryptoCurrenciesList = cryptoCurrenciesSymbolList.reduce { acc, item -> "$acc,$item" }
        return cryptoCompareApiService.fetchMultipleCoinsFullData(cryptoCoinSymbolCsv = cryptoCurrenciesList,
                cryptoCoinsCurrencyRepresentationsCsv = currencyRepresentation.name)
    }
}