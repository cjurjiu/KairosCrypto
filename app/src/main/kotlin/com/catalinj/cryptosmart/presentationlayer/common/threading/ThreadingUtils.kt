package com.catalinj.cryptosmart.presentationlayer.common.threading

import android.os.Looper

fun mainThreadCheck() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw IllegalStateException("Invoked operation must be called from the main thread.")
    }
}