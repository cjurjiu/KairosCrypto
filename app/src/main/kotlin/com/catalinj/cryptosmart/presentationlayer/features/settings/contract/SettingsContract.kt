package com.catalinj.cryptosmart.presentationlayer.features.settings.contract

import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

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