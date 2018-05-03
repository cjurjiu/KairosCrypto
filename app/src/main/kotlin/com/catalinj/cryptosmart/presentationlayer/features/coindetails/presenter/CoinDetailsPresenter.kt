package com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinDetailsContract

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter(private val repository: CoinsRepository) : CoinDetailsContract.CoinDetailsPresenter {

    private var view: CoinDetailsContract.CoinDetailsView? = null

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")
        view?.increaseValue()
    }

    override fun stopPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#stopPresenting")
    }

    override fun viewAvailable(view: CoinDetailsContract.CoinDetailsView) {
        Log.d("Cata", "CoinDetailsPresenter#viewAvailable")
        this.view = view
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
}