package com.catalinjurjiu.kairoscrypto.datalayer.userprefs

import android.content.Context
import android.content.SharedPreferences
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation

/**
 * Created by catalin on 11/05/2018.
 */
class KairosCryptoUserSettingsImpl(context: Context) : KairosCryptoUserSettings {

    private val sharedPrefs: SharedPreferences =
            context.applicationContext.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)

    override fun saveHasDummyBookmarks(hasDummyBookmarks: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_DUMMY_BOOKMARKS, hasDummyBookmarks).apply()
    }

    override fun hasDummyBookmarks(): Boolean {
        return sharedPrefs.getBoolean(KEY_DUMMY_BOOKMARKS, false)
    }

    override fun savePrimaryCurrency(currency: CurrencyRepresentation) {
        val editor = sharedPrefs.edit()
        editor.putString(KEY_PRIMARY_CURRENCY, currency.currency)
        editor.apply()
    }

    override fun getPrimaryCurrency(): CurrencyRepresentation {
        val currency = sharedPrefs.getString(KEY_PRIMARY_CURRENCY, CurrencyRepresentation.USD.currency)
        return CurrencyRepresentation.valueOf(currency)
    }

    override fun saveDarkThemeEnabled(darkThemeEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_DARK_THEME_ENABLED, darkThemeEnabled).apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPrefs.getBoolean(KEY_DARK_THEME_ENABLED, false)
    }

    companion object {
        const val SHARED_PREFS_FILE_NAME = "KairosCryptoPrefs"
        const val KEY_PRIMARY_CURRENCY = "PrimaryCurrency"
        const val KEY_DARK_THEME_ENABLED = "DarkThemeEnabled"
        const val KEY_DUMMY_BOOKMARKS = "DummyBookmarks"
    }
}

interface KairosCryptoUserSettings {

    fun saveHasDummyBookmarks(hasDummyBookmarks: Boolean)

    fun hasDummyBookmarks(): Boolean

    fun savePrimaryCurrency(currency: CurrencyRepresentation)

    fun getPrimaryCurrency(): CurrencyRepresentation

    fun saveDarkThemeEnabled(darkThemeEnabled: Boolean)

    fun isDarkThemeEnabled(): Boolean
}