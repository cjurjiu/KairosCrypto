package com.catalinjurjiu.kairoscrypto.di.modules.coinlist

import com.catalinjurjiu.common.ActiveActivityProvider
import com.catalinjurjiu.kairoscrypto.businesslayer.repository.CoinsRepository
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.CoinListScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.presenter.CoinDisplayOptionsPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.presenter.CoinsListPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view.AndroidResourceDecoder
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.contract.ScrollToTopWidgetContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.presenter.ScrollToTopWidgetPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalinj on 04.02.2018.
 */
@Module
class CoinListModule {

    @Provides
    @CoinListScope
    fun provideCoinsListPresenter(coinsRepository: CoinsRepository,
                                  userSettings: KairosCryptoUserSettings): CoinsListContract.CoinsListPresenter {
        return CoinsListPresenter(repository = coinsRepository,
                userSettings = userSettings)
    }

    @Provides
    @CoinListScope
    fun provideCoinDisplayOptionsPresenter(activeActivityProvider: ActiveActivityProvider,
                                           coinListPresenter: CoinsListContract.CoinsListPresenter,
                                           userSettings: KairosCryptoUserSettings):
            CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter {

        return CoinDisplayOptionsPresenter(coinDisplayController = coinListPresenter,
                userSettings = userSettings,
                resourceDecoder = AndroidResourceDecoder(activeActivityProvider = activeActivityProvider))
    }

    @Provides
    @CoinListScope
    fun provideScrollToTopWidgetPresenter(): ScrollToTopWidgetContract.ScrollToTopWidgetPresenter {
        return ScrollToTopWidgetPresenter()
    }
}