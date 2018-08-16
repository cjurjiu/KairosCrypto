package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter

import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.MarketsRepository
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.threading.Executors
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by catalin on 05/05/2018.
 */
class CoinMarketsPresenter(private val coinData: CoinDetailsPartialData,
                           private val repository: MarketsRepository,
                           parentPresenter: CoinDetailsContract.CoinDetailsPresenter) :
        CoinMarketsContract.CoinMarketsPresenter {

    private var view: CoinMarketsContract.CoinMarketsView? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        parentPresenter.registerChild(coinMarketsPresenter = this)
    }

    override fun startPresenting() {
        compositeDisposable.add(repository.getMarketsListObservable(coinSymbol = coinData.coinSymbol)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //onNext
                    view?.setData(it)
                })
        fetchData()
    }

    private fun fetchData() {
        repository.updateMarketsData(coinSymbol = coinData.coinSymbol,
                webFriendlyName = coinData.webFriendlyName,
                errorHandler = Consumer {
                    Executors.mainThread().execute {
                        view?.showError(errorCode = ErrorCode.GENERIC_ERROR, retryAction = ::fetchData)
                    }
                })
    }

    override fun stopPresenting() {
        //todo - do we need to do anything here?
    }

    override fun viewAvailable(view: CoinMarketsContract.CoinMarketsView) {
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        this.view = null
        this.compositeDisposable.clear()
    }

    override fun getView(): CoinMarketsContract.CoinMarketsView? {
        return view
    }

    override fun handleRefresh(): Boolean {
        fetchData()
        return true
    }
}