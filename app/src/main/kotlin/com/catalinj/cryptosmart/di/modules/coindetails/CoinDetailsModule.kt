package com.catalinj.cryptosmart.di.modules.coindetails;

import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config.CoinMarketCapBookmarksRepositoryConfigurator
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.CoinDetailsScope
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.contract.CoinDetailsContract.CoinDetailsPresenter.CoinDetailsPartialData
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.main.presenter.CoinDetailsPresenter
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
    fun provideBookmarksRepository(database: CryptoSmartDb,
                                   @CoinMarketCapApiQualifier retrofit: Retrofit): BookmarksRepository {

        return CoinMarketCapBookmarksRepositoryConfigurator(database = database, retrofit = retrofit)
                .configure()
    }
}
