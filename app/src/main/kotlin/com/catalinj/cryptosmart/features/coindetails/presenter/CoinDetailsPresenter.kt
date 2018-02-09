package com.catalinj.cryptosmart.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.features.coinslist.contract.CoinDetailsContract
import com.catalinj.cryptosmart.network.CoinMarketCapService
import com.catalinj.cryptosmart.repository.CoinsRepository

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter constructor(db: CryptoSmartDb, cmkService: CoinMarketCapService) : CoinDetailsContract.CoinDetailsPresenter {

    private val repository = CoinsRepository(db, cmkService)
    private lateinit var view: CoinDetailsContract.CoinDetailsView

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")
    }

    override fun stopPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#stopPresenting")
    }

    override fun onViewAvailable(view: CoinDetailsContract.CoinDetailsView) {
        Log.d("Cata", "CoinDetailsPresenter#onViewAvailable")
        this.view = view
    }

    override fun onViewDestroyed() {
        Log.d("Cata", "CoinDetailsPresenter#onViewDestroyed")
    }

    override fun getView(): CoinDetailsContract.CoinDetailsView {
        return view
    }

    override fun userPressedBack(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userPullToRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun receivedFocus() {
        view.increaseValue()
    }
}