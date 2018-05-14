package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.businesslayer.repository.Repository
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by catalin on 05/05/2018.
 */
class CoinInfoPresenter(private val coinsRepository: CoinsRepository,
                        private val cryptoSmartUserSettings: CryptoSmartUserSettings,
                        private val parentPresenter: CoinDetailsContract.CoinDetailsPresenter)
    : CoinInfoContract.CoinInfoPresenter {

    private var view: CoinInfoContract.CoinInfoView? = null
    private lateinit var coinId: String
    private lateinit var coinSymbol: String
    private var availableData: CryptoCoinDetails? = null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val primaryCurrency = CurrencyRepresentation.EUR

    init {
        parentPresenter.registerChild(coinInfoPresenter = this)
    }

    override fun startPresenting() {
        val coinDetailsDisposable = coinsRepository.getCoinDetailsObservable(coinSymbol = coinSymbol)
                .debounce(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { coinDetails ->
                    Log.d("Cata", "CoinInfoPresenter-> coinDetailsObservable#onNext." +
                            "Coin serverId: ${coinDetails.id}")
                    val relevantPriceDataItems = coinDetails.priceData.keys
                            .filter {
                                it == primaryCurrency.currency
                                        || it == CurrencyRepresentation.BTC.currency
                            }
                    if (relevantPriceDataItems.size < 2) {
                        //ignore results that don't have both value representations.
                        //might be just triggers from the database that's updated
                        return@subscribe
                    }
                    //onNext
                    availableData = coinDetails
                    view?.setCoinInfo(coinDetails)
                }

        val loadingStateDisposable = coinsRepository.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //onNext
                    when (it) {
                        Repository.LoadingState.Idle -> parentPresenter.childFinishedLoading()
                        Repository.LoadingState.Loading -> parentPresenter.childStartedLoading()
                    }
                }

        compositeDisposable.add(coinDetailsDisposable)
        compositeDisposable.add(loadingStateDisposable)

        val data = availableData
        if (data != null) {
            view?.setCoinInfo(data)
        } else {
            coinsRepository.fetchCoinDetails(coinId = coinId,
                    valueRepresentationsArray = arrayOf(primaryCurrency, CurrencyRepresentation.BTC),
                    errorHandler = Consumer {
                        Log.d("Cata", "CoinInfoPresenter: Error fetching coin: $it")
                    })
        }
    }

    override fun stopPresenting() {
        compositeDisposable.clear()
    }

    override fun viewAvailable(view: CoinInfoContract.CoinInfoView) {
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        this.view = null
    }

    override fun getView(): CoinInfoContract.CoinInfoView? {
        return view
    }

    override fun setCoinId(coinId: String) {
        this.coinId = coinId
    }

    override fun setCoinSymbol(coinSymbol: String) {
        this.coinSymbol = coinSymbol
    }

    override fun handleRefresh(): Boolean {
        coinsRepository.fetchCoinDetails(coinId = coinId,
                valueRepresentationsArray = arrayOf(primaryCurrency, CurrencyRepresentation.BTC),
                errorHandler = Consumer {
                    Log.d("Cata", "CoinInfoPresenter: Error fetching coin: $it")
                })
        return true
    }

    companion object {
        const val TAG = "CoinInfoPresenter"
    }

}