package com.catalinj.smartpersist.atomics

/**
 * Created by catalin on 05.02.18.
 */
interface HasRetainable<out T : Map<String, Any>> {

    fun getRetainable(): T
}