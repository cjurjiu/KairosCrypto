package com.catalinj.cryptosmart.features

/**
 * Created by catalin on 05.02.18.
 */
interface HasRetainable<out T : Pair<String, Any>> {
    fun getRetainable(): T
}