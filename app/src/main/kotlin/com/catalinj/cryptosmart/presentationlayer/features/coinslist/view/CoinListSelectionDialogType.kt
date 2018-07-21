package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.view.SelectionDialog

sealed class CoinListSelectionDialogType(typeName: String) : SelectionDialog.SelectionDialogIdentifier(identifier = typeName) {

    object ChangeCurrency : CoinListSelectionDialogType(DIALOG_CHANGE_CURRENCY_SELECTION_NAME)
    object SelectSnapshot : CoinListSelectionDialogType(DIALOG_SELECT_SNAPSHOT_SELECTION_NAME)

    companion object {
        private const val DIALOG_CHANGE_CURRENCY_SELECTION_NAME = "ChangeCurrencySelectionDialog"
        private const val DIALOG_SELECT_SNAPSHOT_SELECTION_NAME = "SelectSnapshotSelectionDialog"

        fun children(): Array<CoinListSelectionDialogType> {
            return arrayOf(ChangeCurrency, SelectSnapshot)
        }
    }
}