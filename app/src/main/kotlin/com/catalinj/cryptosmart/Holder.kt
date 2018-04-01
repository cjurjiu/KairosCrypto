@file:Suppress("AddVarianceModifier")

package com.catalinj.cryptosmart

interface Holder<ComponentType : Any> {
    val component: ComponentType

    val hasComp: Boolean
}