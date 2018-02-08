package com.catalinj.cryptosmart.common.atomics

/**
 * Created by catalin on 05.02.18.
 */
interface HasRetainable<out T : Map<String, Any>> {
    fun getRetainable(): T
    operator fun component1(): HasRetainable<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}