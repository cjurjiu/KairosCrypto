package com.catalinjurjiu.common

/**
 * Marker interface for a class that holds a reference to an object instance that needs to be retained
 * across a configuration change.
 *
 * Created by catalin on 05.02.18.
 */
interface HasRetainable<out T : Map<String, Any>> {
    val retainable: T
}