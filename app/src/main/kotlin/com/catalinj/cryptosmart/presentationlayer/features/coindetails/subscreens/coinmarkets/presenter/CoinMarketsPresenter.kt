package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by catalin on 05/05/2018.
 */
class CoinMarketsPresenter(private val coinData: CoinDetailsPartialData,
                           private val repository: MarketsRepository,
                           private val parentPresenter: CoinDetailsContract.CoinDetailsPresenter,
                           private val userSettings: CryptoSmartUserSettings) :
        CoinMarketsContract.CoinMarketsPresenter {

    private var view: CoinMarketsContract.CoinMarketsView? = null
    private val primaryCurrency = userSettings.getPrimaryCurrency()

    init {
        parentPresenter.registerChild(coinMarketsPresenter = this)
    }

    override fun startPresenting() {
        repository.getMarketsListObservable(coinSymbol = coinData.coinSymbol)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.setData(it)
                }
        Log.d("Cata", "Markets presenter #startPresenting.")
        repository.updateMarketsData(coinSymbol = coinData.coinSymbol,
                webFriendlyName = coinData.webFriendlyName)
    }

    override fun stopPresenting() {
        //todo
    }

    override fun viewAvailable(view: CoinMarketsContract.CoinMarketsView) {
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        this.view = null
    }

    override fun getView(): CoinMarketsContract.CoinMarketsView? {
        return view
    }

    override fun handleRefresh(): Boolean {
        //todo
        return false
    }
}