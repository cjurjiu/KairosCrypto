package com.catalinj.cryptosmart.presentationlayer.common.extension

import android.widget.TextView
import com.catalinj.cryptosmart.R

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