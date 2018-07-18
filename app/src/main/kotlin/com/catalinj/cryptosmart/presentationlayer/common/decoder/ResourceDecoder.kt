package com.catalinj.cryptosmart.presentationlayer.common.decoder

import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

/**
 * Created by catalin on 22/04/2018.
 */
interface ResourceDecoder {

    fun decodeString(identifier: StringResource): String

    fun decodeStringArray(identifier: StringArrayResource): List<String>

    fun decodeSelectionItems(desiredSelectionItems: SelectionItemsResource): List<SelectionItem>
}

enum class StringResource {
    //todo
}

enum class StringArrayResource {
    SNAPSHOTS,
    CURRENCIES
}

enum class SelectionItemsResource {
    SNAPSHOTS,
    CURRENCIES
}