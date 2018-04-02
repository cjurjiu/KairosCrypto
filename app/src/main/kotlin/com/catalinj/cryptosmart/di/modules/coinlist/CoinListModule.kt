package com.catalinj.cryptosmart.di.modules.coinlist

import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.scopes.CoinListScope
import com.catalinj.cryptosmart.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.features.coinslist.presenter.CoinsListPresenter
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class CoinListModule {

    @CoinListScope
    @Provides
    fun provideCoinsListPresenter(db: CryptoSmartDb,
                                  coinMarketCapService: CoinMarketCapService): CoinsListContract.CoinsListPresenter {
        return CoinsListPresenter(db = db, coinMarketCapService = coinMarketCapService)
    }
}