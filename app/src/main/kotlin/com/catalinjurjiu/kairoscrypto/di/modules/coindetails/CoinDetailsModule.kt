package com.catalinjurjiu.kairoscrypto.di.modules.coindetails;

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapBookmarksRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.RestServiceFactory
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinDetailsScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.presenter.CoinDetailsPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 08.02.2018.
 */
@Module
class CoinDetailsModule(private val coinDetailsPartialData: CoinDetailsPartialData) {

    @Provides
    @CoinDetailsScope
    fun provideCoinDetailsPresenter(bookmarksRepository: BookmarksRepository): CoinDetailsContract.CoinDetailsPresenter {
        return CoinDetailsPresenter(coinDetailsPartialData, bookmarksRepository)
    }

    @Provides
    @CoinDetailsScope
    fun provideBookmarksRepository(database: KairosCryptoDb,
                                   restServiceFactory: RestServiceFactory): BookmarksRepository {
        return CoinMarketCapBookmarksRepository(kairosCryptoDb = database,
                coinMarketCapApiService = restServiceFactory.getCoinsRestServiceApi())
    }
}