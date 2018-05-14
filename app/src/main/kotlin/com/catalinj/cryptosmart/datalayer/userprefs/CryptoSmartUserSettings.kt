package com.catalinj.cryptosmart.datalayer.userprefs

import android.content.Context
import android.content.SharedPreferences
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation

/**
 * Created by catalin on 11/05/2018.
 */
class CryptoSmartUserSettingsImpl(context: Context) : CryptoSmartUserSettings {
    private val sharedPrefs: SharedPreferences =
            context.applicationContext.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)

    override fun savePrimaryCurrency(currency: CurrencyRepresentation) {
        val editor = sharedPrefs.edit()
        editor.putString(KEY_PRIMARY_CURRENCY, currency.currency)
        editor.apply()
    }

    override fun getPrimaryCurrency(): CurrencyRepresentation {
        val currency = sharedPrefs.getString(KEY_PRIMARY_CURRENCY, "USD")
        return CurrencyRepresentation.valueOf(currency)
    }

    companion object {
        const val SHARED_PREFS_FILE_NAME = "CryptoSmartPrefs"
        const val KEY_PRIMARY_CURRENCY = "PrimaryCurrency"
    }
}

interface CryptoSmartUserSettings {

    fun savePrimaryCurrency(currency: CurrencyRepresentation)

    fun getPrimaryCurrency(): CurrencyRepresentation
}
