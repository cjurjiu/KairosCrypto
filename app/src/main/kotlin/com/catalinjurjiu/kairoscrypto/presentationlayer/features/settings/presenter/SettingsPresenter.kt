package com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.presenter

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.contract.SettingsContract

class SettingsPresenter(private val userSettings: KairosCryptoUserSettings) : SettingsContract.SettingsPresenter {

    private var view: SettingsContract.SettingsView? = null

    private var primaryCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()

    override fun startPresenting() {
        //nothing to do here
    }

    override fun stopPresenting() {
        //nothing to do here
    }

    override fun viewAvailable(view: SettingsContract.SettingsView) {
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        this.view = null
    }

    override fun getView(): SettingsContract.SettingsView? {
        return view
    }

    override fun darkThemeSettingChanged(darkThemeChecked: Boolean) {
        userSettings.saveDarkThemeEnabled(darkThemeEnabled = darkThemeChecked)
        view?.applyNewTheme()
    }

    override fun primaryCurrencyChanged(primaryCurrency: CurrencyRepresentation) {
        if (this.primaryCurrency == primaryCurrency) {
            //discard redundant changes
            return
        }
        userSettings.savePrimaryCurrency(primaryCurrency)
    }

    override fun getCurrentPrimaryCurrency(): CurrencyRepresentation {
        return userSettings.getPrimaryCurrency()
    }

    override fun gerCurrenciesList(): Array<CurrencyRepresentation> {
        return CurrencyRepresentation.values()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return userSettings.isDarkThemeEnabled()
    }
}