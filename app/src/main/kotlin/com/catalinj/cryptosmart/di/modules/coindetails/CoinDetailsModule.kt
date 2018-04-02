package com.catalinj.cryptosmart.di.modules.coindetails;

import com.catalinj.cryptosmart.datastorage.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.features.coinslist.contract.CoinDetailsContract
import com.catalinj.cryptosmart.features.coinslist.presenter.CoinDetailsPresenter
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapService
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 08.02.2018.
 */
@Module
class CoinDetailsModule {

    @CoinDetailsScope
    @Provides
    fun provideCoinDetailsPresenter(db: CryptoSmartDb,
                                    coinMarketCapService: CoinMarketCapService): CoinDetailsContract.CoinDetailsPresenter {
        return CoinDetailsPresenter(db = db, coinMarketCapService = coinMarketCapService)
    }
}
