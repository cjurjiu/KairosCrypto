package com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.contract

import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView


/**
 * Contract which defines the view-presenter interactions that occur in the Settings screen.
 *
 * Created by catalin on 31/05/2018.
 */
interface SettingsContract {

    /**
     * Interface definition for a presenter in charge of the Settings screen.
     */
    interface SettingsPresenter : MvpPresenter<SettingsPresenter, SettingsView> {

        /**
         * Called by the view when the user enables or disables the dark theme setting.
         *
         * @param darkThemeChecked a boolean indicating if the dark theme is enabled or not
         */
        fun darkThemeSettingChanged(darkThemeChecked: Boolean)

        /**
         * Called by the view when the user changes the primary currency to a new value.
         *
         * @param primaryCurrency the new [CurrencyRepresentation] to be used as a primary currency
         */
        fun primaryCurrencyChanged(primaryCurrency: CurrencyRepresentation)

        /**
         * Provides the currently active primary currency.
         *
         * @return a [CurrencyRepresentation] object which represents the current primary currency
         */
        fun getCurrentPrimaryCurrency(): CurrencyRepresentation

        /**
         * Provides a list of available currencies to be used as primary currencies.
         *
         * @return an array of [CurrencyRepresentation] which can be selected by the user
         */
        fun gerCurrenciesList(): Array<CurrencyRepresentation>

        /**
         * Provides info whether the dark theme is enabled or not.
         *
         * @return `true` if the dark theme is enabled, `false` otherwise
         */
        fun isDarkThemeEnabled(): Boolean
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * Settings screen.
     */
    interface SettingsView : MvpView<SettingsPresenter, SettingsView> {

        /**
         * Called by the Presenter to inform this view that the theme has changed, and that the new
         * theme needs to be applied.
         */
        fun applyNewTheme()
    }
}