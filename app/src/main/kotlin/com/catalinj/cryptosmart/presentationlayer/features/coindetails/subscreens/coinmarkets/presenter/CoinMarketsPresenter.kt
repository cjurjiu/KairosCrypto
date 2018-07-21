package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.threading.Executors
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

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
        Log.d("Cata", "Markets presenter #startPresenting.")
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
        //todo
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

    override fun viewScrolled(currentScrollPosition: Int, maxScrollPosition: Int) {
        //scroll to top button hide/reveal logic
        val displayedItemPosition = (view?.getDisplayedItemPosition() ?: 0)
        val scrollToTopVisible = view?.isScrollToTopVisible() ?: false
        if (displayedItemPosition > SCROLL_TO_TOP_LIST_THRESHOLD && !scrollToTopVisible) {
            view?.revealScrollToTopButton()
        } else if (displayedItemPosition < SCROLL_TO_TOP_LIST_THRESHOLD && scrollToTopVisible) {
            view?.hideScrollToTopButton()
        }
    }

    override fun scrollToTopPressed() {
        view?.scrollTo(0)
        view?.hideScrollToTopButton()
    }

    private companion object {
        const val SCROLL_TO_TOP_LIST_THRESHOLD = 30
    }

}