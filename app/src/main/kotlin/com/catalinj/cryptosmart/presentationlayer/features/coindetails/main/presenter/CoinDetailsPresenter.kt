package com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinDetailsPresenter(private val repository: CoinsRepository) : CoinDetailsContract.CoinDetailsPresenter {

    private var view: CoinDetailsContract.CoinDetailsView? = null
    private lateinit var coinId: String
    private var availableData: CryptoCoinDetails? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        Log.d("Cata", "Injected CoinDetailsPresenter")
    }

    override fun setCoinId(coinId: String) {
        this.coinId = coinId
    }

    override fun startPresenting() {
        Log.d("Cata", "CoinDetailsPresenter#startPresenting")

        val coinDetailsDisposable = repository.getCoinDetailsObservable(coinId = coinId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("Cata", "CoinDetailsPresenter-> coinDetailsObservable#onNext." +
                            "Coin id: ${it.id}")

                    //onNext
                    availableData = it
                    view?.setCoinData(it)
                }
        compositeDisposable.add(coinDetailsDisposable)

        val data = availableData
        if (data != null) {
            view?.setCoinData(data)
        } else {
            repository.fetchCoinDetails(coinId, Consumer {
                Log.d("Cata", "CoinDetailsPresenter: Error fetching coin: $it")
            })
        }
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

    override fun userPressedBack(): Boolean {
        Log.d("Cata", "CoinDetailsPresenter#userPressedBack")
        return false
    }

    override fun userPullToRefresh() {
        Log.d("Cata", "CoinDetailsPresenter#userPullToRefresh")
        //TODO
    }
}