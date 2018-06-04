package com.catalinj.cryptosmart.di.modules.coindetails;

import com.catalinj.cryptosmart.di.annotations.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.presenter.CoinDetailsPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 08.02.2018.
 */
@Module
class CoinDetailsModule(private val coinDetailsPartialData: CoinDetailsPartialData) {

    @Provides
    @CoinDetailsScope
    fun provideCoinDetailsPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return CoinDetailsPresenter(coinDetailsPartialData)
    }
}
