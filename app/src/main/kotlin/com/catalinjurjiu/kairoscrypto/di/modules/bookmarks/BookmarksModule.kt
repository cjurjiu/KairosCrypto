package com.catalinjurjiu.kairoscrypto.di.modules.bookmarks

import android.content.Context
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.config.CoinMarketCapBookmarksRepositoryConfigurator
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.ActivityContext
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.CoinMarketCapApiQualifier
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.BookmarksScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.presenter.BookmarksPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.presenter.CoinDisplayOptionsPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view.AndroidResourceDecoder
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.contract.ScrollToTopWidgetContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.presenter.ScrollToTopWidgetPresenter
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
    fun provideBookmarksPresenter(bookmarksRepository: BookmarksRepository,
                                  userSettings: KairosCryptoUserSettings)
            : BookmarksContract.BookmarksPresenter {
        return BookmarksPresenter(bookmarksRepository = bookmarksRepository,
                userSettings = userSettings)
    }

    @Provides
    @BookmarksScope
    fun provideCoinDisplayOptionsPresenter(@ActivityContext context: Context,
                                           bookmarksPresenter: BookmarksContract.BookmarksPresenter,
                                           userSettings: KairosCryptoUserSettings)
            : CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter {

        return CoinDisplayOptionsPresenter(resourceDecoder = AndroidResourceDecoder(context = context),
                coinDisplayController = bookmarksPresenter,
                userSettings = userSettings)
    }

    @Provides
    @BookmarksScope
    fun provideBookmarksRepository(database: KairosCryptoDb,
                                   @CoinMarketCapApiQualifier retrofit: Retrofit): BookmarksRepository {

        return CoinMarketCapBookmarksRepositoryConfigurator(database = database, retrofit = retrofit)
                .configure()
    }

    @Provides
    @BookmarksScope
    fun provideScrollToTopWidgetPresenter(): ScrollToTopWidgetContract.ScrollToTopWidgetPresenter {
        return ScrollToTopWidgetPresenter()
    }
}