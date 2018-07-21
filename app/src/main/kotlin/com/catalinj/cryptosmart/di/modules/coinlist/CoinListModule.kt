package com.catalinj.cryptosmart.di.modules.coinlist

import android.content.Context
import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.ActivityContext
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinListScope
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.presenter.CoinDisplayOptionsPresenter
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.presenter.CoinsListPresenter
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.AndroidResourceDecoder
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class CoinListModule {

    @Provides
    @CoinListScope
    fun provideCoinsListPresenter(@CoinMarketCapApiQualifier coinsRepository: CoinsRepository,
                                  userSettings: CryptoSmartUserSettings): CoinsListContract.CoinsListPresenter {
        return CoinsListPresenter(repository = coinsRepository,
                userSettings = userSettings)
    }

    @Provides
    @CoinListScope
    fun provideCoinDisplayOptionsPresenter(@ActivityContext context: Context,
                                           coinListPresenter: CoinsListContract.CoinsListPresenter,
                                           userSettings: CryptoSmartUserSettings):
            CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter {

        return CoinDisplayOptionsPresenter(coinDisplayController = coinListPresenter,
                userSettings = userSettings,
                resourceDecoder = AndroidResourceDecoder(context = context))
    }
}