package com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model

import com.catalinjurjiu.common.NamedComponent

/**
 * Created by catalin on 24/04/2018.
 */
data class SelectionItem(override val name: String,
                         val value: String,
                         var isActive: Boolean = false) : NamedComponent