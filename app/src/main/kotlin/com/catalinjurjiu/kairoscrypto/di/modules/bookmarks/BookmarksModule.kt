package com.catalinjurjiu.kairoscrypto.di.modules.bookmarks

import com.catalinjurjiu.common.ActiveActivityProvider
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.BookmarksRepository
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.coinmarketcap.CoinMarketCapBookmarksRepository
import com.catalinjurjiu.kairoscrypto.datalayer.database.contract.KairosCryptoDb
import com.catalinjurjiu.kairoscrypto.datalayer.network.RestServiceFactory
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
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
    fun provideCoinDisplayOptionsPresenter(activeActivityProvider: ActiveActivityProvider,
                                           bookmarksPresenter: BookmarksContract.BookmarksPresenter,
                                           userSettings: KairosCryptoUserSettings)
            : CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter {

        val resDecoder = AndroidResourceDecoder(activeActivityProvider = activeActivityProvider)
        return CoinDisplayOptionsPresenter(resourceDecoder = resDecoder,
                coinDisplayController = bookmarksPresenter,
                userSettings = userSettings)
    }

    @Provides
    @BookmarksScope
    fun provideBookmarksRepository(database: KairosCryptoDb,
                                   restServiceFactory: RestServiceFactory): BookmarksRepository {

        return CoinMarketCapBookmarksRepository(kairosCryptoDb = database,
                coinMarketCapApiService = restServiceFactory.getCoinsRestServiceApi())
    }

    @Provides
    @BookmarksScope
    fun provideScrollToTopWidgetPresenter(): ScrollToTopWidgetContract.ScrollToTopWidgetPresenter {
        return ScrollToTopWidgetPresenter()
    }
}