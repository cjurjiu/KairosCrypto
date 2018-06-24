package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.content.Context
import android.support.annotation.ArrayRes
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import java.util.*

class CoinListResourceDecoder(context: Context) : ResourceDecoder(context) {

    fun decodeChangeCoinDialogItems(primaryCurrency: CurrencyRepresentation): List<SelectionItem> {
        val selectionList = decodeSelectionItemList(
                visibleValuesResId = R.array.change_currency_dialog_options,
                valuesSymbolsResId = R.array.change_currency_dialog_options_values)
        val primaryCurrencyName = Currency.getInstance(primaryCurrency.currency).displayName
        selectionList.add(index = 0, element = SelectionItem(name = primaryCurrencyName, value = primaryCurrency.currency))
        return selectionList
    }

    fun fetchSortOptionsDialogItems(): List<SelectionItem> {
        return decodeSelectionItemList(
                visibleValuesResId = R.array.sort_list_dialog_options,
                valuesSymbolsResId = R.array.sort_list_dialog_options_values)
    }

    fun decodeSnapshotDialogItems(): List<SelectionItem> {
        return decodeSelectionItemList(
                visibleValuesResId = R.array.snapshot_list_dialog_options,
                valuesSymbolsResId = R.array.snapshot_dialog_options_values)
    }

    private fun decodeSelectionItemList(@ArrayRes visibleValuesResId: Int,
                                        @ArrayRes valuesSymbolsResId: Int): MutableList<SelectionItem> {

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
