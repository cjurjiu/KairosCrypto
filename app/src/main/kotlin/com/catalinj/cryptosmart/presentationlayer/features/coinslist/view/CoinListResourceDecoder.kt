package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.content.Context
import android.support.annotation.ArrayRes
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

class CoinListResourceDecoder(context: Context) : ResourceDecoder(context) {

    fun decodeChangeCoinDialogItems(markedActive: String = ""): List<SelectionItem> {
        return decodeSelectionItemList(
                visibleValuesResId = R.array.change_currency_dialog_options,
                valuesSymbolsResId = R.array.change_currency_dialog_options_values,
                markedAsActiveItemSymbol = markedActive)
    }

    fun fetchSortOptionsDialogItems(markedActive: String = ""): List<SelectionItem> {
        return decodeSelectionItemList(
                visibleValuesResId = R.array.sort_list_dialog_options,
                valuesSymbolsResId = R.array.sort_list_dialog_options_values,
                markedAsActiveItemSymbol = markedActive)
    }

    fun decodeSnapshotDialogItems(markedActive: String = ""): List<SelectionItem> {
        return decodeSelectionItemList(
                visibleValuesResId = R.array.snapshot_list_dialog_options,
                valuesSymbolsResId = R.array.snapshot_dialog_options_values,
                markedAsActiveItemSymbol = markedActive)
    }

    private fun decodeSelectionItemList(@ArrayRes visibleValuesResId: Int,
                                        @ArrayRes valuesSymbolsResId: Int,
                                        markedAsActiveItemSymbol: String): List<SelectionItem> {

        val visibleValues: Array<String> = resources.getStringArray(visibleValuesResId)
        val visibleValuesSymbols = resources.getStringArray(valuesSymbolsResId)

        val selectionItemList = mutableListOf<SelectionItem>()
        for (i in 0 until visibleValues.size) {
            selectionItemList.add(
                    SelectionItem(name = visibleValues[i],
                            value = visibleValuesSymbols[i],
                            activeItem = (visibleValuesSymbols[i] == markedAsActiveItemSymbol))
            )
        }
        return selectionItemList
    }
}
