package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

sealed class SelectionDialogType(val typeName: String) {

    object ChangeCurrency : SelectionDialogType(DIALOG_CHANGE_CURRENCY_SELECTION_NAME)
    object SortModes : SelectionDialogType(DIALOG_SORT_LIST_SELECTION_NAME)
    object SelectSnapshot : SelectionDialogType(DIALOG_SELECT_SNAPSHOT_SELECTION_NAME)
}

const val DIALOG_CHANGE_CURRENCY_SELECTION_NAME = "ChangeCurrencySelectionDialog"
const val DIALOG_SORT_LIST_SELECTION_NAME = "SortListSelectionDialog"
const val DIALOG_SELECT_SNAPSHOT_SELECTION_NAME = "SelectSnapshotSelectionDialog"
