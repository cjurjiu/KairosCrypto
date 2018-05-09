package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by catalin on 05/05/2018.
 */
class CoinInfoPresenter(private val coinsRepository: CoinsRepository,
                        private val parentPresenter: CoinDetailsContract.CoinDetailsPresenter)
    : CoinInfoContract.CoinInfoPresenter {

    private var view: CoinInfoContract.CoinInfoView? = null
    private lateinit var coinId: String
    private lateinit var coinSymbol: String

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var availableData: CryptoCoinDetails? = null

    init {
        parentPresenter.registerChild(coinInfoPresenter = this)
    }

    override fun startPresenting() {
        val coinDetailsDisposable = coinsRepository.getCoinDetailsObservable(coinSymbol = coinSymbol)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("Cata", "CoinInfoPresenter-> coinDetailsObservable#onNext." +
                            "Coin serverId: ${it.id}")
                    //onNext
                    availableData = it
                    view?.setCoinInfo(it)
                }

        val loadingStateDisposable = coinsRepository.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //onNext
                    when (it) {
                        is RequestState.Idle -> parentPresenter.childFinishedLoading()
                        is RequestState.Loading -> parentPresenter.childStartedLoading()
                        else -> Log.d(TAG, "Received unknown loading state")
                    }
                }

        compositeDisposable.add(coinDetailsDisposable)
        compositeDisposable.add(loadingStateDisposable)

        val data = availableData
        if (data != null) {
            view?.setCoinInfo(data)
        } else {
            coinsRepository.fetchCoinDetails(coinId, Consumer {
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
        coinsRepository.fetchCoinDetails(coinId, Consumer {
            Log.d("Cata", "CoinInfoPresenter: Error fetching coin: $it")
        })
        return true
    }

    companion object {
        const val TAG = "CoinInfoPresenter"
    }

}