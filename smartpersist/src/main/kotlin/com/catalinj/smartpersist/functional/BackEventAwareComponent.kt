package com.catalinj.smartpersist.functional

/**
 * Created by catalinj on 08.02.2018.
 */
@FunctionalInterface
interface BackEventAwareComponent {
    fun onBack(): Boolean
}