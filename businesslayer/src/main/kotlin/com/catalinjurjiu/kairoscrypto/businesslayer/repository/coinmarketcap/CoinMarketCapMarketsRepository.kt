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
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinMarketCapMarketsRepository(private val kairosCryptoDb: KairosCryptoDb,
                                     private val coinMarketCapHtmlService: CoinMarketCapHtmlService,
                                     private val userSettings: KairosCryptoUserSettings) : MarketsRepository {

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

    override fun updateMarketsData(coinSymbol: String, webFriendlyName: String, errorHandler: Consumer<Throwable>) {
        coinMarketCapHtmlService.fetchCoinMarkets(webFriendlyName)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val parser = MarketInfoHtmlParser(coinSymbol = coinSymbol,
                            marketInfoHtmlPage = it)
                    val markets = parser.extractMarkets().map { it.toDataLayerMarketInto() }
                    Log.w("Cata", "Add markets to DB. size: ${markets.size}")
                    val addedIds = kairosCryptoDb.getMarketsInfoDao().insert(markets)
                    Log.w("Cata", "Add markets markets with ids:$addedIds")
                }, { it ->
                    //onError
                    errorHandler.accept(it)
                })
    }
}