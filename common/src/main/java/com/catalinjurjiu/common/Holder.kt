package com.catalinjurjiu.common

/**
 * Interface for a class which hold a reference to a particular object type.
 */
@Suppress("AddVarianceModifier")
interface Holder<ComponentType : Any> {
    val component: ComponentType

    val hasComp: Boolean
}