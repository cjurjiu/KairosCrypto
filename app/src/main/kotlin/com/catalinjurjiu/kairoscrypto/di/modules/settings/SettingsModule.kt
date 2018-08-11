package com.catalinjurjiu.kairoscrypto.di.modules.settings

import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.SettingsScope
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.contract.SettingsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.presenter.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    @SettingsScope
    fun provideSettingsPresenter(userSettings: KairosCryptoUserSettings): SettingsContract.SettingsPresenter {
        return SettingsPresenter(userSettings = userSettings)
    }
}