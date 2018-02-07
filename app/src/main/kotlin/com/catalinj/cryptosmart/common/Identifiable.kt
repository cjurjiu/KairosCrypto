package com.catalinj.cryptosmart.common

/**
 * Created by catalin on 06.02.18.
 */
interface Identifiable<out T> {
    fun getIdentity(): T
}