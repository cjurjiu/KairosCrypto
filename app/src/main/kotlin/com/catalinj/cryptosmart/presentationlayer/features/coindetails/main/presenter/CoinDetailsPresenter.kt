package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.presenter

import android.util.Log
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter(private var coinPartialData: CoinDetailsPartialData) : CoinDetailsContract.CoinDetailsPresenter {
    override var navigator: Navigator? = null

    private var view: CoinDetailsContract.CoinDetailsView? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var childCoinInfoPresenter: CoinInfoContract.CoinInfoPresenter? = null
    private var childCoinMarketsPresenter: CoinMarketsContract.CoinMarketsPresenter? = null

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")
        updateView(data = coinPartialData)
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
        view = null
        navigator = null
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

    override fun getCoinData(): CoinDetailsPartialData {
        return coinPartialData
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
        this.coinPartialData = CoinDetailsPartialData(
                coinName = this.coinPartialData.coinName,
                webFriendlyName = this.coinPartialData.webFriendlyName,
                coinSymbol = this.coinPartialData.coinSymbol,
                coinId = this.coinPartialData.coinId,
                change1h = newChange1h)
        //update view after change
        updateView(data = coinPartialData)
    }

    override fun userPressedBack() {
        navigator?.navigateBack()
    }

    private fun updateView(data: CoinDetailsPartialData) {
        view?.setCoinInfo(data.coinName,
                data.coinSymbol,
                data.change1h)
    }

    private companion object {
        const val TAG = "CoinDetailsPresenter"
    }

}