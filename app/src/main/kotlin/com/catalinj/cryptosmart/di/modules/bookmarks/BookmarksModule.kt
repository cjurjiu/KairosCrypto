package com.catalinj.cryptosmart.di.modules.bookmarks

import android.content.Context
import com.catalinj.cryptosmart.businesslayer.repository.BookmarksRepository
import com.catalinj.cryptosmart.businesslayer.repository.coinmarketcap.config.CoinMarketCapBookmarksRepositoryConfigurator
import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.qualifiers.ActivityContext
import com.catalinj.cryptosmart.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinj.cryptosmart.di.annotations.scopes.BookmarksScope
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter.BookmarksPresenter
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.view.AndroidResourceDecoder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by catalin on 14/05/2018.
 */
@Module
class BookmarksModule {

    @Provides
    @BookmarksScope
    fun provideBookmarksPresenter(@ActivityContext context: Context,
                                  bookmarksRepository: BookmarksRepository,
                                  userSettings: CryptoSmartUserSettings)
            : BookmarksContract.BookmarksPresenter {
        return BookmarksPresenter(resourceDecoder = AndroidResourceDecoder(context = context),
                bookmarksRepository = bookmarksRepository,
                userSettings = userSettings)
    }

    @Provides
    @BookmarksScope
    fun provideBookmarksRepository(database: CryptoSmartDb,
                                   @CoinMarketCapApiQualifier retrofit: Retrofit): BookmarksRepository {

        return CoinMarketCapBookmarksRepositoryConfigurator(database = database, retrofit = retrofit)
                .configure()
    }
}