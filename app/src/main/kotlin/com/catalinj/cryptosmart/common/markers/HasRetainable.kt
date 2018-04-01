package com.catalinj.cryptosmart.common.markers

/**
 * Created by catalin on 05.02.18.
 */
interface HasRetainable<out T : Map<String, Any>> {
    val retainable: T
}