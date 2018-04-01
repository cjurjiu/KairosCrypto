package com.catalinj.cryptosmart

import android.arch.lifecycle.ViewModel

class DaggerComponentStore<T : Any> : ViewModel(), Holder<T> {

    override lateinit var component: T

    override val hasComp: Boolean
        get() = ::component.isInitialized
}