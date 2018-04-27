package com.catalinj.cryptosmart.presentationlayer.common.functional

/**
 * Created by catalinj on 08.02.2018.
 */
@FunctionalInterface
interface BackEventAwareComponent {
    fun onBack(): Boolean
}