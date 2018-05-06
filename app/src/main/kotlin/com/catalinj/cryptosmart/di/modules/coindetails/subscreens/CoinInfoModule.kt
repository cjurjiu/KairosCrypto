package com.catalinj.cryptosmart.di.modules.coindetails.subscreens

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinInfoScope
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
    fun provideCoinInfoPresenter(@CoinMarketCapQualifier repository: CoinsRepository): CoinInfoContract.CoinInfoPresenter {
        return CoinInfoPresenter(coinsRepository = repository)
    }
}