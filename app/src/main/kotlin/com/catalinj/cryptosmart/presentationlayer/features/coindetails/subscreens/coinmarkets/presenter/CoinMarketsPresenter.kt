package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract

/**
 * Created by catalin on 05/05/2018.
 */
class CoinMarketsPresenter(private val coinId: String,
                           private val repository: MarketsRepository,
                           private val parentPresenter: CoinDetailsContract.CoinDetailsPresenter) :
        CoinMarketsContract.CoinMarketsPresenter {

    init {
        parentPresenter.registerChild(coinMarketsPresenter = this)
    }

    override fun startPresenting() {
        parentPresenter.getCoinId()
        Log.d("Cata", "Markets presenter #startPresenting.")
        repository.updateMarketsData("bitcoin")
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