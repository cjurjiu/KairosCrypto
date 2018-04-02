package com.catalinjurjiu.common

/**
 * Represents a simple factory interface.
 */
@FunctionalInterface
interface Factory<out T> {
    fun create(): T
}