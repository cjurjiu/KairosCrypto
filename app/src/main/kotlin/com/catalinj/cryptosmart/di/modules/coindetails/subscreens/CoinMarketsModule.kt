package com.catalinj.cryptosmart.di.modules.coindetails.subscreens

import com.catalinj.cryptosmart.businesslayer.repository.MarketsRepository
import com.catalinj.cryptosmart.config.CoinMarketCapMarketsRepositoryConfigurator
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinMarketsScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter.CoinMarketsPresenter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by catalin on 06/05/2018.
 */
@Module
class CoinMarketsModule(private val partialCoinData: CoinDetailsPartialData) {

    @Provides
    @CoinMarketsScope
    fun provideCoinMarketsPresenter(coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter,
                                    repository: MarketsRepository,
                                    userSettings: CryptoSmartUserSettings)
            : CoinMarketsContract.CoinMarketsPresenter {

        return CoinMarketsPresenter(coinData = partialCoinData,
                repository = repository,
                parentPresenter = coinDetailsPresenter,
                userSettings = userSettings)
    }

    @Provides
    @CoinMarketsScope
    fun provideMarketsRepository(database: CryptoSmartDb,
                                 @CoinMarketCapHtmlQualifier retrofit: Retrofit,
                                 userSettings: CryptoSmartUserSettings)
            : MarketsRepository {

        return CoinMarketCapMarketsRepositoryConfigurator(database = database,
                retrofit = retrofit,
                userSettings = userSettings).configure()
    }
}