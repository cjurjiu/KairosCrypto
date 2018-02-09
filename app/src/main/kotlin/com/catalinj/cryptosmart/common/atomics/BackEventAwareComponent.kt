package com.catalinj.cryptosmart.common.atomics

/**
 * Created by catalinj on 08.02.2018.
 */
interface BackEventAwareComponent {
    fun onBack(): Boolean
}