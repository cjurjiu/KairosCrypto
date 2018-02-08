package com.catalinj.cryptosmart.common.atomics

/**
 * Created by catalin on 06.02.18.
 */
interface Identifiable<out T> {
    fun getIdentity(): T
}