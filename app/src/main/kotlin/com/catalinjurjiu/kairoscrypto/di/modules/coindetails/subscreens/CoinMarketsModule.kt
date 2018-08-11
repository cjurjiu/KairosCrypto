package com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.MarketsRepository
import com.catalinjurjiu.kairoscrypto.config.CoinMarketCapMarketsRepositoryConfigurator
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapHtmlQualifier
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinMarketsScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter.CoinMarketsPresenter
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
                                    userSettings: KairosCryptoUserSettings)
            : CoinMarketsContract.CoinMarketsPresenter {

        return CoinMarketsPresenter(coinData = partialCoinData,
                repository = repository,
                parentPresenter = coinDetailsPresenter,
                userSettings = userSettings)
    }

    @Provides
    @CoinMarketsScope
    fun provideMarketsRepository(database: KairosCryptoDb,
                                 @CoinMarketCapHtmlQualifier retrofit: Retrofit,
                                 userSettings: KairosCryptoUserSettings)
            : MarketsRepository {

        return CoinMarketCapMarketsRepositoryConfigurator(database = database,
                retrofit = retrofit,
                userSettings = userSettings).configure()
    }
}