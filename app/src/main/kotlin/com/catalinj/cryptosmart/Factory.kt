package com.catalinj.cryptosmart

@FunctionalInterface
interface Factory<out T> {
    fun create(): T
}