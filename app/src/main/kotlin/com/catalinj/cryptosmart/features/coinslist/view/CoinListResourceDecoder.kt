package com.catalinj.cryptosmart.features.coinslist.view

import android.content.Context
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.features.selectiondialog.model.SelectionItem

class CoinListResourceDecoder(context: Context) : ResourceDecoder(context) {

    fun decodeChangeCoinListItems(): List<SelectionItem> {
        val changeSelectionDialogOptions: Array<String> =
                resources.getStringArray(R.array.change_currency_dialog_options)
        val changeSelectionDialogValues: Array<String> =
                resources.getStringArray(R.array.change_currency_dialog_options_values)

        return listOf(SelectionItem(changeSelectionDialogOptions[0], changeSelectionDialogValues[0]),
                SelectionItem(changeSelectionDialogOptions[1], changeSelectionDialogValues[1]))
    }

    fun decodeSortListItems(): List<SelectionItem> {
        val sortSelectionDialogListItems: Array<String> =
                resources.getStringArray(R.array.sort_list_dialog_options)
        val sortSelectionDialogListValues =
                resources.getStringArray(R.array.sort_list_dialog_options_values)

        val selectionItemList = mutableListOf<SelectionItem>()
        for (i in 0 until sortSelectionDialogListItems.size) {
            selectionItemList.add(
                    SelectionItem(sortSelectionDialogListItems[i], sortSelectionDialogListValues[i])
            )
        }
        return selectionItemList
    }

    fun decodeSnapShotListItems(): List<SelectionItem> {
        val snapShotDialogListItems: Array<String> =
                resources.getStringArray(R.array.snapshot_list_dialog_options)
        val snapShotDialogListValues =
                resources.getStringArray(R.array.snapshot_dialog_options_values)

        val selectionItemList = mutableListOf<SelectionItem>()
        for (i in 0 until snapShotDialogListItems.size) {
            selectionItemList.add(
                    SelectionItem(snapShotDialogListItems[i], snapShotDialogListValues[i])
            )
        }
        return selectionItemList
    }
}
