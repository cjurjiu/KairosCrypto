package com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.converter.toBusinessLayerMarketInfo
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

class CoinMarketCapMarketsRepository(private val cryptoSmartDb: CryptoSmartDb,
                                     private val coinMarketCapHtmlService: CoinMarketCapHtmlService,
                                     private val userSettings: CryptoSmartUserSettings) : MarketsRepository {

    override val loadingStateObservable: Observable<Repository.LoadingState> = Observable.empty()

    override fun getMarketsListObservable(coinSymbol: String): Observable<List<CryptoCoinMarketInfo>> {
        return cryptoSmartDb.getMarketsInfoDao()
                .getMarketsObservable(coinSymbol = coinSymbol)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .toObservable()
                .map { dbMarketInfoList ->
                    dbMarketInfoList.map { dbMarketInfo -> dbMarketInfo.toBusinessLayerMarketInfo() }
                }
    }

    override fun updateMarketsData(websiteSlug: String) {
        coinMarketCapHtmlService.fetchCoinMarkets(websiteSlug)
                .subscribeOn(Schedulers.io())
                .doOnError { Log.e("Cata", "Error", it) }
                .subscribe(Consumer {
                    Log.d("Cata", "Markets Response: $it")
                    val jsoupParser = Jsoup.parse(it)
                    Log.d("Cata", "JsoupObject: ${jsoupParser.title()}")
                })
    }
}