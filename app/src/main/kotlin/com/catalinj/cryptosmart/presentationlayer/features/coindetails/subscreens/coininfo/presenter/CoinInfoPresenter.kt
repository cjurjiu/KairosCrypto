package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by catalin on 05/05/2018.
 */
class CoinInfoPresenter(private val coinsRepository: CoinsRepository) : CoinInfoContract.CoinInfoPresenter {

    private var view: CoinInfoContract.CoinInfoView? = null
    private lateinit var coinId: String
    private lateinit var coinSymbol: String

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var availableData: CryptoCoinDetails? = null

    override fun startPresenting() {
        val coinDetailsDisposable = coinsRepository.getCoinDetailsObservable(coinId = coinId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("Cata", "CoinInfoPresenter-> coinDetailsObservable#onNext." +
                            "Coin id: ${it.id}")

                    //onNext
                    availableData = it
                    view?.setCoinInfo(it)
                }
        compositeDisposable.add(coinDetailsDisposable)

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

    override fun handleRefresh() {
        coinsRepository.fetchCoinDetails(coinId, Consumer {
            Log.d("Cata", "CoinInfoPresenter: Error fetching coin: $it")
        })
    }

    companion object {
        const val TAG = "CoinInfoPresenter"
    }

}