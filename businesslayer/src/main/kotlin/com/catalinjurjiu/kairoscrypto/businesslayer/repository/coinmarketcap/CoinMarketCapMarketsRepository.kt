package com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toBusinessLayerMarketInfo
import com.catalinjurjiu.kairoscrypto.businesslayer.converter.toDataLayerMarketInto
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.MarketsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * [MarketsRepository] implementation that uses CoinMarketCap.com as prime provider of data.
 *
 * This repository implementation uses an offline-first approach: it first stores the coins in the
 * database, and only provides data from the database, never directly from the network.
 *
 * This repository does not use a REST api to obtain the markets data. Rather, it fetches the HTML
 * page on CoinMarketCap.com which stores market data for a particular coin, and parses it with a
 * [MarketInfoHtmlParser] to obtain [CryptoCoinMarketInfo] objects.
 *
 * @constructor Creates a new instance of this repository.
 * @param kairosCryptoDb database object used to store the coins fetched over the network.
 * @param coinMarketCapHtmlService service used to fetch the coins from CoinMarketCap.com
 *
 * Created by catalinj on 28.01.2018.
 */
class CoinMarketCapMarketsRepository(private val kairosCryptoDb: KairosCryptoDb,
                                     private val coinMarketCapHtmlService: CoinMarketCapHtmlService)
    : MarketsRepository {

    override val loadingStateObservable: Observable<Repository.LoadingState> = Observable.empty()

    override fun getMarketsListObservable(coinSymbol: String): Observable<List<CryptoCoinMarketInfo>> {
        return kairosCryptoDb.getMarketsInfoDao()
                .getMarketsObservable(coinSymbol = coinSymbol)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .toObservable()
                .map { dbMarketInfoList ->
                    dbMarketInfoList.map { dbMarketInfo -> dbMarketInfo.toBusinessLayerMarketInfo() }
                }
    }

    override fun fetchMarketsData(coinSymbol: String, webFriendlyName: String, errorHandler: Consumer<Throwable>) {
        coinMarketCapHtmlService.fetchCoinMarkets(webFriendlyName)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val parser = MarketInfoHtmlParser(coinSymbol = coinSymbol,
                            marketInfoHtmlPage = it)
                    val markets = parser.extractMarkets()
                            .map { it.toDataLayerMarketInto() }
                    Log.w(TAG, "Add markets to DB. size: ${markets.size}")
                    val addedIds = kairosCryptoDb.getMarketsInfoDao().insert(markets)
                    Log.w(TAG, "Add markets markets with ids:$addedIds")
                }, { it ->
                    //onError
                    errorHandler.accept(it)
                })
    }

    private companion object {
        const val TAG = "CMCMarketsRepository"
    }
}