package com.catalinjurjiu.kairoscrypto.di.modules.coindetails;

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.config.CoinMarketCapBookmarksRepositoryConfigurator
import com.catalinjurjiu.kairoscrypto.datalayer.database.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinDetailsScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindetails.main.presenter.CoinDetailsPresenter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

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
                                   @CoinMarketCapApiQualifier retrofit: Retrofit): BookmarksRepository {

        return CoinMarketCapBookmarksRepositoryConfigurator(database = database, retrofit = retrofit)
                .configure()
    }
}
