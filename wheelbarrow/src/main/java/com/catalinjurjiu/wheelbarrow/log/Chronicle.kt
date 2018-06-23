package com.catalinjurjiu.wheelbarrow.log

import android.util.Log

internal object Chronicle {

    private var LOG_PREFIX = "WheelBarrow"

    fun logInfo(tag: String, message: String) {
        Log.i("$LOG_PREFIX$tag", message)
    }

    fun logDebug(tag: String = "", message: String) {
        Log.d("$LOG_PREFIX$tag", message)
    }

    fun logWarn(tag: String = "", message: String) {
        Log.w("$LOG_PREFIX$tag", message)
    }

    fun logError(tag: String = "", message: String) {
        Log.e("$LOG_PREFIX$tag", message)
    }
}