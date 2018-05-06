package com.catalinj.cryptosmart.di.modules.coindetails.subscreens

import com.catalinj.cryptosmart.di.annotations.scopes.CoinMarketsScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter.CoinMarketsPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 06/05/2018.
 */
@Module
class CoinMarketsModule(private val coinId: String) {

    @Provides
    @CoinMarketsScope
    fun provideCoinMarketsPresenter(coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter)
            : CoinMarketsContract.CoinMarketsPresenter {
        return CoinMarketsPresenter(coinId = coinId, parentPresenter = coinDetailsPresenter)
    }
}