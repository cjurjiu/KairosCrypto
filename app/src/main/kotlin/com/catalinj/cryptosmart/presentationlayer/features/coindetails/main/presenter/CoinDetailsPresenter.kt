package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.presenter

import android.util.Log
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter : CoinDetailsContract.CoinDetailsPresenter {

    private var view: CoinDetailsContract.CoinDetailsView? = null
    private lateinit var coinId: String
    private lateinit var coinSymbol: String
    private lateinit var coinName: String
    private var coinChange1h: Float = 0F
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var childCoinInfoPresenter: CoinInfoContract.CoinInfoPresenter? = null
    private var childCoinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter? = null

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun setInitialInfo(coinName: String, coinSymbol: String, coinId: String, change1h: Float) {
        this.coinId = coinId
        this.coinName = coinName
        this.coinSymbol = coinSymbol
        this.coinChange1h = change1h
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")
        view?.setCoinInfo(coinName, coinSymbol, coinChange1h)
    }

    override fun stopPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#stopPresenting")
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: CoinDetailsContract.CoinDetailsView) {
        Log.d("Cata", "CoinDetailsPresenter#viewAvailable")
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        Log.d("Cata", "CoinDetailsPresenter#viewDestroyed")
        this.view = null
    }

    override fun getView(): CoinDetailsContract.CoinDetailsView? {
        return view
    }

    override fun userPullToRefresh() {
        Log.d("Cata", "CoinDetailsPresenter#userPullToRefresh")
        val activeView = view?.getActiveChildView()
        when (activeView) {
            is CoinInfoContract.CoinInfoView ->
                childCoinInfoPresenter?.handleRefresh()
            is CoinMarketsContract.CoinMarketsPresenter ->
                childCoinMarketsPresenter?.handleRefresh()
            else -> Log.d(TAG, "Received unknown view")
        }
    }

    override fun getCoinId(): String {
        return coinId
    }

    override fun getCoinSymbol(): String {
        return coinSymbol
    }

    override fun registerChild(coinInfoPresenter: CoinInfoContract.CoinInfoPresenter) {
        this.childCoinInfoPresenter = coinInfoPresenter
    }

    override fun registerChild(coinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter) {
        this.childCoinMarketsPresenter = coinMarketsPresenter
    }

    override fun childStartedLoading() {
        view?.showLoadingIndicator()
    }

    override fun childFinishedLoading() {
        view?.hideLoadingIndicator()
    }

    override fun updateChange1h(newChange1h: Float) {
        this.coinChange1h = newChange1h
        view?.setCoinInfo(coinName = coinName,
                coinSymbol = coinSymbol,
                change1h = newChange1h)
    }

    private companion object {
        const val TAG = "CoinDetailsPresenter"
    }

}