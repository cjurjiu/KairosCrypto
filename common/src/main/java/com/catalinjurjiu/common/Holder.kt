@file:Suppress("AddVarianceModifier")

package com.catalinjurjiu.common

interface Holder<ComponentType : Any> {
    val component: ComponentType

    val hasComp: Boolean
}