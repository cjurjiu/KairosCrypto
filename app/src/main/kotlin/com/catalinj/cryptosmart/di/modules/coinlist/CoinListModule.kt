package com.catalinj.cryptosmart.di.modules.coinlist

import android.content.Context
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinListScope
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter.CoinsListPresenter
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.CoinListResourceDecoder
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class CoinListModule {

    @Provides
    @CoinListScope
    fun provideCoinsListPresenter(context: Context,
                                  userSettings: CryptoSmartUserSettings,
                                  @CoinMarketCapQualifier coinsRepository: CoinsRepository): CoinsListContract.CoinsListPresenter {
        return CoinsListPresenter(CoinListResourceDecoder(context = context),
                userSettings = userSettings,
                repository = coinsRepository)
    }
}