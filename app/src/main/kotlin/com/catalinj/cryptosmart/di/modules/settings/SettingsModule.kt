package com.catalinj.cryptosmart.di.modules.settings

import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.scopes.SettingsScope
import com.catalinj.cryptosmart.presentationlayer.features.settings.contract.SettingsContract
import com.catalinj.cryptosmart.presentationlayer.features.settings.presenter.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    @SettingsScope
    fun provideSettingsPresenter(userSettings: CryptoSmartUserSettings): SettingsContract.SettingsPresenter {
        return SettingsPresenter(userSettings = userSettings)
    }
}