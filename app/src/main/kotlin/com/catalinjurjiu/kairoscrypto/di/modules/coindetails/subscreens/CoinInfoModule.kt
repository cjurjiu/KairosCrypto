package com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinInfoScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coininfo.presenter.CoinInfoPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 06/05/2018.
 */
@Module
class CoinInfoModule {

    @Provides
    @CoinInfoScope
    fun provideCoinInfoPresenter(repository: CoinsRepository,
                                 kairosCryptoUserSettings: KairosCryptoUserSettings,
                                 coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter): CoinInfoContract.CoinInfoPresenter {
        return CoinInfoPresenter(coinsRepository = repository,
                userSettings = kairosCryptoUserSettings,
                parentPresenter = coinDetailsPresenter)
    }
}