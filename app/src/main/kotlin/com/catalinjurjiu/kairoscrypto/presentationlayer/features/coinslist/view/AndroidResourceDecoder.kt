package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ArrayRes
import com.catalinjurjiu.common.ActiveActivityProvider
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.decoder.ResourceDecoder
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.decoder.SelectionItemsResource
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.decoder.StringArrayResource
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.decoder.StringResource
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.model.SelectionItem

class AndroidResourceDecoder(private val activeActivityProvider: ActiveActivityProvider) : ResourceDecoder {

    override fun decodeString(identifier: StringResource): String {
        //nothing for the moment
        return ""
    }

    override fun decodeStringArray(identifier: StringArrayResource): List<String> {
        //nothing for the moment
        return emptyList()
    }

    override fun decodeSelectionItems(desiredSelectionItems: SelectionItemsResource): List<SelectionItem> {
        return when (desiredSelectionItems) {
            SelectionItemsResource.SNAPSHOTS -> decodeSnapshotDialogItems()
            SelectionItemsResource.CURRENCIES -> decodeChangeCoinDialogItems()
        }
    }

    private val resources: Resources = getUpToDateContext().resources

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

    private fun getUpToDateContext(): Context = activeActivityProvider.activeActivity
}