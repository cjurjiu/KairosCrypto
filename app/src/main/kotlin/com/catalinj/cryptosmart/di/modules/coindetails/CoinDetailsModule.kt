package com.catalinj.cryptosmart.di.modules.coindetails;

import com.catalinj.cryptosmart.businesslayer.repository.CoinsRepository
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.presenter.CoinDetailsPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 08.02.2018.
 */
@Module
class CoinDetailsModule {

    @Provides
    @CoinDetailsScope
    fun provideCoinDetailsPresenter(@CoinMarketCapQualifier coinsRepository: CoinsRepository)
            : CoinDetailsContract.CoinDetailsPresenter {
        return CoinDetailsPresenter(repository = coinsRepository)
    }
}
