package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapHtmlService
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import retrofit2.Retrofit

/**
 * Created by catalin on 05/05/2018.
 */
class CoinMarketsPresenter(private val coinId: String,
                           @CoinMarketCapHtmlQualifier private val retrofit: Retrofit,
                           private val parentPresenter: CoinDetailsContract.CoinDetailsPresenter) :
        CoinMarketsContract.CoinMarketsPresenter {

    init {
        parentPresenter.registerChild(coinMarketsPresenter = this)
    }

    override fun startPresenting() {
        Log.d("Cata", "Markets presenter #startPresenting.")

        val service = retrofit.create(CoinMarketCapHtmlService::class.java)
        service.fetchCoinMarkets("bitcoin")
                .subscribeOn(Schedulers.io())
                .doOnError { Log.e("Cata", "Error", it) }
                .subscribe(Consumer {
                    Log.d("Cata", "Markets Response: $it")
                    val jsoupParser = Jsoup.parse(it)
                    Log.d("Cata", "JsoupObject: ${jsoupParser.title()}")
                })
    }

    override fun stopPresenting() {
        //todo
    }

    override fun viewAvailable(view: CoinMarketsContract.CoinMarketsView) {
        //todo
    }

    override fun viewDestroyed() {
        //todo
    }

    override fun getView(): CoinMarketsContract.CoinMarketsView? {
        //todo
        return null
    }

    override fun handleRefresh(): Boolean {
        //todo
        return false
    }
}