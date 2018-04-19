package com.catalinj.cryptosmart.repository

import android.util.Log
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.datastorage.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.network.RequestState
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.network.coinmarketcap.request.BoundedCryptoCoinsApiReq
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by catalinj on 28.01.2018.
 */

class CoinsRepository(private val cryptoSmartDb: CryptoSmartDb,
                      private val coinMarketCapService: CoinMarketCapService) {

    val cryptoCoinObservable: Observable<List<CoinMarketCapCryptoCoin>>
        get() {
            return cryptoCoinsRelay
        }
    val loadingStateObservable: Observable<RequestState>

    private val cryptoCoinsRelay = BehaviorRelay.create<List<CoinMarketCapCryptoCoin>>()
    private val loadingStateRelay = BehaviorRelay.create<RequestState>()

    init {
        loadingStateObservable = getLoadingObservable()
        cryptoSmartDb.getCoinMarketCapCryptoCoinDao().monitorRx().subscribe({
            Log.d("RxJ", "monitor onNext")
            cryptoCoinsRelay.accept(it.map { coin -> convertDbCoinToRestCoin(coin) })
        })
    }

    fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>) {
        val apiRequest = BoundedCryptoCoinsApiReq(startIndex = startIndex,
                numberOfCoins = numberOfCoins,
                coinMarketCapService = coinMarketCapService)

        apiRequest.response.observeOn(Schedulers.io()).subscribe {
            Log.d("RxJ", "repo getFreshCoins response do next coins size:" + it.size)
            val dbCoins: List<DbCryptoCoin> = it.map { coin -> convertRestCoinToDbCoin(coin) }
            cryptoSmartDb.getCoinMarketCapCryptoCoinDao().insert(dbCoins)
            Log.d("RxJ", "repo getFreshCoins response AFTER do next coins size:" + it.size)
        }
        apiRequest.errors.subscribe(errorHandler)
        apiRequest.state.subscribe { loadingStateRelay.accept(it) }
        apiRequest.execute()
    }

    private fun getLoadingObservable(): Observable<RequestState> {
        return loadingStateRelay.map { requestState ->
            return@map when (requestState) {
                RequestState.Idle -> 0
                RequestState.Loading -> 1
                is RequestState.Completed<*>, is RequestState.Error -> -1
            }
        }
                .scan { t1, t2 -> t1 + t2 }
                .map { intResult ->
                    Log.d("RxJ", "repo loading state result: $intResult")
                    return@map if (intResult > 0) {
                        RequestState.Loading
                    } else {
                        RequestState.Idle
                    }
                }.distinctUntilChanged()
    }

}