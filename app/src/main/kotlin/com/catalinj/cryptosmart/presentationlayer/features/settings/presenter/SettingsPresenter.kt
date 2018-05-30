package com.catalinj.cryptosmart.presentationlayer.features.settings.presenter

import android.util.Log
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.features.settings.contract.SettingsContract

class SettingsPresenter(private val userSettings: CryptoSmartUserSettings) : SettingsContract.SettingsPresenter {

    private var view: SettingsContract.SettingsView? = null

    private var primaryCurrency: CurrencyRepresentation = userSettings.getPrimaryCurrency()

    override fun startPresenting() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopPresenting() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        Log.d("Cata", "User darkThemeSettingChanged: $darkThemeChecked")
        userSettings.saveDarkThemeEnabled(darkThemeEnabled = darkThemeChecked)
        view?.applyNewTheme()
    }

    override fun primaryCurrencyChanged(primaryCurrency: CurrencyRepresentation) {
        if (this.primaryCurrency == primaryCurrency) {
            //discard redundant changes
            return
        }
        Log.d("Cata", "User selected new primary currency: $primaryCurrency")
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