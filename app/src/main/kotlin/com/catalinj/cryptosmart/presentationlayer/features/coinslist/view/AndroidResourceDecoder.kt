package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ArrayRes
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.common.decoder.SelectionItemsResource
import com.catalinj.cryptosmart.presentationlayer.common.decoder.StringArrayResource
import com.catalinj.cryptosmart.presentationlayer.common.decoder.StringResource
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

class AndroidResourceDecoder(context: Context) : ResourceDecoder {

    override fun decodeString(identifier: StringResource): String {
        //todo
        return ""
    }

    override fun decodeStringArray(identifier: StringArrayResource): List<String> {
        //todo
        return emptyList()
    }

    override fun decodeSelectionItems(desiredSelectionItems: SelectionItemsResource): List<SelectionItem> {
        return when (desiredSelectionItems) {
            SelectionItemsResource.SNAPSHOTS -> decodeSnapshotDialogItems()
            SelectionItemsResource.CURRENCIES -> decodeChangeCoinDialogItems()
        }
    }

    private val resources: Resources = context.resources

    private fun decodeChangeCoinDialogItems(): List<SelectionItem> =
            decodeSelectionItemList(visibleValuesResId = R.array.change_currency_dialog_options,
                    valuesSymbolsResId = R.array.change_currency_dialog_options_values)


    private fun decodeSnapshotDialogItems(): List<SelectionItem> =
            decodeSelectionItemList(visibleValuesResId = R.array.snapshot_list_dialog_options,
                    valuesSymbolsResId = R.array.snapshot_dialog_options_values)


    private fun decodeSelectionItemList(@ArrayRes visibleValuesResId: Int,
                                        @ArrayRes valuesSymbolsResId: Int): List<SelectionItem> {

        val visibleValues: Array<String> = resources.getStringArray(visibleValuesResId)
        val visibleValuesSymbols = resources.getStringArray(valuesSymbolsResId)

        val selectionItemList = mutableListOf<SelectionItem>()
        for (i in 0 until visibleValues.size) {
            selectionItemList.add(
                    SelectionItem(name = visibleValues[i], value = visibleValuesSymbols[i])
            )
        }
        return selectionItemList
    }
}
