package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.view

import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.view.SelectionDialog

sealed class BookmarksSelectionDialogType(typeName: String) : SelectionDialog.SelectionDialogIdentifier(identifier = typeName) {

    object ChangeCurrency : BookmarksSelectionDialogType(DIALOG_CHANGE_CURRENCY_SELECTION_NAME)
    object SelectSnapshot : BookmarksSelectionDialogType(DIALOG_SELECT_SNAPSHOT_SELECTION_NAME)

    companion object {
        private const val DIALOG_CHANGE_CURRENCY_SELECTION_NAME = "ChangeCurrencySelectionDialog"
        private const val DIALOG_SELECT_SNAPSHOT_SELECTION_NAME = "SelectSnapshotSelectionDialog"

        fun children(): Array<BookmarksSelectionDialogType> {
            return arrayOf(ChangeCurrency, SelectSnapshot)
        }
    }
}