package com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension

import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.FragmentActivity
import android.view.View

fun View.getSupportActivity(): FragmentActivity? {
    var context: Context? = context
    while (context is ContextWrapper) {
        if (context is FragmentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}