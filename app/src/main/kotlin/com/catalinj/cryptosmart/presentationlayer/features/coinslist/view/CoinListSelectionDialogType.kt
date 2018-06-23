package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

sealed class CoinListSelectionDialogType(val typeName: String) {

    object ChangeCurrency : CoinListSelectionDialogType(DIALOG_CHANGE_CURRENCY_SELECTION_NAME)
    object SelectSnapshot : CoinListSelectionDialogType(DIALOG_SELECT_SNAPSHOT_SELECTION_NAME)
}

const val DIALOG_CHANGE_CURRENCY_SELECTION_NAME = "ChangeCurrencySelectionDialog"
const val DIALOG_SELECT_SNAPSHOT_SELECTION_NAME = "SelectSnapshotSelectionDialog"
