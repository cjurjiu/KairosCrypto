package com.catalinj.cryptosmart.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.features.coinslist.contract.CoinDetailsContract
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import com.catalinj.cryptosmart.repository.CoinsRepository

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter(db: CryptoSmartDb, coinMarketCapService: CoinMarketCapService) : CoinDetailsContract.CoinDetailsPresenter {

    private val repository = CoinsRepository(db, coinMarketCapService)
    private var view: CoinDetailsContract.CoinDetailsView? = null

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")
    }

    override fun stopPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#stopPresenting")
    }

    override fun viewAvailable(view: CoinDetailsContract.CoinDetailsView) {
        Log.d("Cata", "CoinDetailsPresenter#viewAvailable")
        this.view = view
    }

    override fun viewInitialised() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewDestroyed() {
        Log.d("Cata", "CoinDetailsPresenter#viewDestroyed")
        this.view = null
    }

    override fun getView(): CoinDetailsContract.CoinDetailsView {
        return view!!
    }

    override fun userPressedBack(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userPullToRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun receivedFocus() {
        view?.increaseValue()
    }
}