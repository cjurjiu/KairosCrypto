package com.catalinjurjiu.kairoscrypto.presentationlayer.common.formatter

import android.content.Context
import android.support.v4.os.ConfigurationCompat
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.threading.mainThreadCheck
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {

    private lateinit var locale: Locale

    private val cachedFormattersMap: MutableMap<String, NumberFormat> = mutableMapOf()

    fun refreshLocale(context: Context) {
        mainThreadCheck()
        locale = ConfigurationCompat.getLocales(context.applicationContext.resources.configuration).get(0)
        cachedFormattersMap.clear()
    }

    fun format(value: Float, currencyRepresentation: CurrencyRepresentation): String {
        return format(value.toDouble(), currencyRepresentation)
    }

    fun format(value: Double, currencyRepresentation: CurrencyRepresentation): String {
        mainThreadCheck()
        val formatter = if (currencyRepresentation == CurrencyRepresentation.BTC) {
            getBitcoinFormatter(minFractionOfDigits = getBitcoinMinNumberOfFractionDigits(value),
                    maxFractionDigits = getBitcoinMaxNumberOfFractionDigits(value))
        } else {
            getFiatCurrencyFormatter(currencyRepresentation, getFiatNumberOfFractionDigits(value))
        }
        return formatter.format(value)
    }

    fun formatCrypto(value: Double, symbol: String): String {
        mainThreadCheck()
        return getCryptocurrencyFormatter(symbol, minFractionOfDigits = 0, maxFractionDigits = 10).format(value)
    }

    private fun getFiatCurrencyFormatter(currencyRepresentation: CurrencyRepresentation, maxFractionDigits: Int): NumberFormat {
        val formatterKey = computeFormatterKey(currencyRepresentation.currency, maxFractionDigits)
        return if (cachedFormattersMap.containsKey(formatterKey)) {
            cachedFormattersMap[formatterKey]!!
        } else {
            val formatter = NumberFormat.getCurrencyInstance(locale)
            formatter.currency = Currency.getInstance(currencyRepresentation.currency)
            formatter.minimumFractionDigits = 2
            formatter.maximumFractionDigits = maxFractionDigits
            formatter.minimumIntegerDigits = 1
            cachedFormattersMap[formatterKey] = formatter
            formatter
        }
    }

    private fun computeFormatterKey(coinSymbol: String, maxFractionDigits: Int) =
            "$coinSymbol$maxFractionDigits"

    private fun getBitcoinFormatter(minFractionOfDigits: Int, maxFractionDigits: Int): NumberFormat {
        return getCryptocurrencyFormatter(coinSymbol = CurrencyRepresentation.BTC.currency,
                minFractionOfDigits = minFractionOfDigits,
                maxFractionDigits = maxFractionDigits)
    }

    private fun getCryptocurrencyFormatter(coinSymbol: String,
                                           minFractionOfDigits: Int,
                                           maxFractionDigits: Int): NumberFormat {

        val formatterKey = computeFormatterKey(coinSymbol, 0)
        val formatter = if (cachedFormattersMap.containsKey(formatterKey)) {
            cachedFormattersMap[formatterKey]!!
        } else {
            val formatter = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
            val decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale)
            decimalFormatSymbols.currencySymbol = coinSymbol
            formatter.decimalFormatSymbols = decimalFormatSymbols
            formatter.minimumIntegerDigits = 1
            cachedFormattersMap[formatterKey] = formatter
            formatter
        }
        formatter.minimumFractionDigits = minFractionOfDigits
        formatter.maximumFractionDigits = maxFractionDigits
        return formatter
    }

    private fun getFiatNumberOfFractionDigits(value: Double): Int {
        val valueAbs = Math.abs(value)
        return when {
            valueAbs > 1 -> 2
            valueAbs > 0.1 -> 3
            valueAbs > 0.01 -> 4
            valueAbs > 0.001 -> 5
            valueAbs > 0.0001 -> 6
            valueAbs > 0.00001 -> 7
            valueAbs > 0.000001 -> 8
            else -> 8
        }
    }

    private fun getBitcoinMinNumberOfFractionDigits(value: Double): Int {
        val valueAbs = Math.abs(value)
        return when {
            valueAbs > 1 -> 0
            else -> 8
        }
    }

    private fun getBitcoinMaxNumberOfFractionDigits(value: Double): Int {
        val valueAbs = Math.abs(value)
        return when {
            valueAbs > 1 -> 2
            else -> 8
        }
    }
}