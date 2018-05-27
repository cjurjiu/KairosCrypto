package com.catalinj.cryptosmart.di.modules.coindetails.subscreens

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinInfoScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.presenter.CoinInfoPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 06/05/2018.
 */
@Module
class CoinInfoModule {

    @Provides
    @CoinInfoScope
    fun provideCoinInfoPresenter(@CoinMarketCapApiQualifier repository: CoinsRepository,
                                 cryptoSmartUserSettings: CryptoSmartUserSettings,
                                 coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter): CoinInfoContract.CoinInfoPresenter {
        return CoinInfoPresenter(coinsRepository = repository,
                cryptoSmartUserSettings = cryptoSmartUserSettings,
                parentPresenter = coinDetailsPresenter)
    }
}