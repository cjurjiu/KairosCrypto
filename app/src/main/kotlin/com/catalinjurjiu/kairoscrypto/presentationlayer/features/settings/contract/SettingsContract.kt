package com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.contract

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView

interface SettingsContract {

    interface SettingsPresenter : MvpPresenter<SettingsPresenter, SettingsView> {

        fun darkThemeSettingChanged(darkThemeChecked: Boolean)

        fun primaryCurrencyChanged(primaryCurrency: CurrencyRepresentation)

        fun getCurrentPrimaryCurrency(): CurrencyRepresentation

        fun gerCurrenciesList(): Array<CurrencyRepresentation>

        fun isDarkThemeEnabled(): Boolean
    }

    interface SettingsView : MvpView<SettingsPresenter, SettingsView> {

        fun applyNewTheme()
    }
}