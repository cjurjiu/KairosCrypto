package com.catalinjurjiu.common

@FunctionalInterface
interface Factory<out T> {
    fun create(): T
}