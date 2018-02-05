package com.catalinj.cryptosmart.features.coinslist.presenter

import com.catalinj.cryptosmart.common.Executors
import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.network.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.network.CoinMarketCapService
import com.catalinj.cryptosmart.repository.CoinsRepository

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListPresenter(db: CryptoSmartDb, cmkService: CoinMarketCapService) : CoinsListContract.CoinsListPresenter {

    private val repository = CoinsRepository(db, cmkService)
    private lateinit var view: CoinsListContract.CoinsListView

    override fun startPresenting() {
        //fetch coins
        Executors.networkIO().execute {
            val coins: List<CoinMarketCapCryptoCoin> = repository.getCoins()
            println("coins received! size is:${coins.size}")
            Executors.mainThread().execute {
                view.setListData(coins)
            }
        }
    }

    override fun stopPresenting() {
        //todo, do something
    }

    override fun onViewAvailable(view: CoinsListContract.CoinsListView) {
        this.view = view
    }

    override fun onViewDestroyed() {
        //todo, do something
    }

    override fun getView(): CoinsListContract.CoinsListView {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun coinSelected(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userPullToRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}