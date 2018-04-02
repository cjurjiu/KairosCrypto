package com.catalinjurjiu.common

/**
 * Created by catalinj on 03.02.2018.
 */
interface Configurator<out T> {
    fun configure(): T
}