package com.catalinj.cryptosmart.presentationlayer.common.decoder

import android.content.Context
import android.content.res.Resources

/**
 * Created by catalin on 22/04/2018.
 */
abstract class ResourceDecoder(private val context: Context) {
    protected val resources: Resources = context.resources
}