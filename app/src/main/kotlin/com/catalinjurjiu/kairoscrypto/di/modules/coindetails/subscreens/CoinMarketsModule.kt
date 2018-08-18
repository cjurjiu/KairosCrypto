package com.catalinjurjiu.kairoscrypto.di.modules.coindetails.subscreens

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.MarketsRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapMarketsRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.HtmlServiceFactory
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinMarketsScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.subscreens.coinmarkets.presenter.CoinMarketsPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.contract.ScrollToTopWidgetContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.presenter.ScrollToTopWidgetPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 06/05/2018.
 */
@Module
class CoinMarketsModule(private val partialCoinData: CoinDetailsPartialData) {

    @Provides
    @CoinMarketsScope
    fun provideCoinMarketsPresenter(coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter,
                                    repository: MarketsRepository)
            : CoinMarketsContract.CoinMarketsPresenter {

        return CoinMarketsPresenter(coinData = partialCoinData,
                repository = repository,
                parentPresenter = coinDetailsPresenter)
    }

    @Provides
    @CoinMarketsScope
    fun provideMarketsRepository(database: KairosCryptoDb,
                                 restServiceFactory: HtmlServiceFactory): MarketsRepository {

        return CoinMarketCapMarketsRepository(kairosCryptoDb = database,
                coinMarketCapHtmlService = restServiceFactory.getMarketsHtmlServiceApi())
    }

    @Provides
    @CoinMarketsScope
    fun provideScrollToTopWidgetPresenter(): ScrollToTopWidgetContract.ScrollToTopWidgetPresenter {
        return ScrollToTopWidgetPresenter()
    }
}