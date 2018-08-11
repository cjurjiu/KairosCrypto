package com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension

import android.widget.TextView
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.formatter.CurrencyFormatter

private const val NUMBER_OF_PERCENT_DECIMALS = 2

fun TextView.displayPercent(percent: Float, colored: Boolean = true) {
    text = String.format("%.${NUMBER_OF_PERCENT_DECIMALS}f%%", percent)
    if (colored) {
        if (percent < 0) {
            setTextColor(resources.getColor(R.color.red_primary))
        } else {
            setTextColor(resources.getColor(R.color.green_primary))
        }
    }
}

fun TextView.displayAsPercent(initialValue: Float, updateValue: Float) {
    val percent = updateValue / initialValue
    text = String.format("%.${NUMBER_OF_PERCENT_DECIMALS}f%%", percent)
    if (percent < 0) {
        setTextColor(resources.getColor(R.color.red_primary))
    } else {
        setTextColor(resources.getColor(R.color.green_primary))
    }
}

fun TextView.percentAsAbsoluteChange(initialValue: Float,
                                     percentChange: Float,
                                     currency: CurrencyRepresentation,
                                     colored: Boolean = true) {
    val absoluteChange = initialValue * percentChange
    text = CurrencyFormatter.format(value = absoluteChange,
            currencyRepresentation = currency)
    if (colored) {
        if (percentChange < 0) {
            setTextColor(resources.getColor(R.color.red_primary))
        } else {
            setTextColor(resources.getColor(R.color.green_primary))
        }
    }
}