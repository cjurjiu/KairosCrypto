package com.catalinj.cryptosmart.features.selectiondialog.model

import com.catalinjurjiu.common.NamedComponent

/**
 * Created by catalin on 24/04/2018.
 */
data class SelectionItem(override val name: String,
                         val value: String,
                         val activeItem: Boolean = false) : NamedComponent