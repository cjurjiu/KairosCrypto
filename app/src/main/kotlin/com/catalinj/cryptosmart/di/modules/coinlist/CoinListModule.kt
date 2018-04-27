package com.catalinj.cryptosmart.di.modules.coinlist

import android.content.Context
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.scopes.CoinListScope
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter.CoinsListPresenter
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinListResourceDecoder
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.CoinMarketCapService
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class CoinListModule {

    @CoinListScope
    @Provides
    fun provideCoinsListPresenter(context: Context,
                                  db: CryptoSmartDb,
                                  coinMarketCapService: CoinMarketCapService): CoinsListContract.CoinsListPresenter {
        return CoinsListPresenter(
                CoinListResourceDecoder(context = context),
                db = db,
                coinMarketCapService = coinMarketCapService)
    }
}