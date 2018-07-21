package com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.presenter

import android.util.Log
import com.catalinj.cryptosmart.businesslayer.model.PredefinedSnapshot
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.presentationlayer.common.decoder.ResourceDecoder
import com.catalinj.cryptosmart.presentationlayer.common.decoder.SelectionItemsResource
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

class CoinDisplayOptionsPresenter(private val coinDisplayController: CoinsDisplayOptionsContract.CoinDisplayController,
                                  private val userSettings: CryptoSmartUserSettings,
                                  private val resourceDecoder: ResourceDecoder) :
        CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter {

    private var view: CoinsDisplayOptionsContract.CoinsDisplayOptionsView? = null
    private lateinit var changeCurrencyDialogItems: List<SelectionItem>
    private lateinit var changeSnapshotDialogItems: List<SelectionItem>

    override fun startPresenting() {
        Log.d("Cata", "Toolbar Presenter: startPresenting")

        initDisplayCurrencyDialogItems()
        initSnapshotDialogOptions()
    }

    override fun stopPresenting() {
        Log.d("Cata", "Toolbar Presenter: stopPresenting")
        //don't need anything here as of now
    }

    override fun viewAvailable(view: CoinsDisplayOptionsContract.CoinsDisplayOptionsView) {
        Log.d("Cata", "Toolbar Presenter: viewAvailable")
        this.view = view
        view.initialise()
    }

    override fun viewDestroyed() {
        Log.d("Cata", "Toolbar Presenter: viewDestroyed")
        this.view = null
    }

    override fun getView(): CoinsDisplayOptionsContract.CoinsDisplayOptionsView? = view

    override fun changeCurrencyButtonPressed() {
        view?.openChangeCurrencyDialog(selectionItems = changeCurrencyDialogItems)
    }

    override fun selectSnapshotButtonPressed() {
        view?.openSelectSnapshotDialog(changeSnapshotDialogItems)
    }

    override fun displayCurrencyChanged(newSelectedCurrency: SelectionItem) {
        if (newSelectedCurrency.value == coinDisplayController.displayCurrency.currency) {
            //the new selection is equal to the currency of the display controller. do nothing
            return
        }
        coinDisplayController.displayCurrency = CurrencyRepresentation.valueOf(newSelectedCurrency.value.toUpperCase())
        refreshActiveCurrencyForSelectionList(selectionList = changeCurrencyDialogItems)
        Log.d("Cata", "displayCurrencyChanged: selectionItem:${newSelectedCurrency.name}")
    }

    override fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem) {
        if (newSelectedSnapshot.value == coinDisplayController.displaySnapshot.stringValue) {
            //the new selection is equal to the snapshot of the display controller. do nothing
            return
        }
        coinDisplayController.displaySnapshot = PredefinedSnapshot.of(newSelectedSnapshot.value)
        refreshSelectedSnapshot(selectionList = changeSnapshotDialogItems)
        Log.d("Cata", "selectedSnapshotChanged: selectionItem:${newSelectedSnapshot.name}")
    }

    private fun initDisplayCurrencyDialogItems() {
        if (::changeCurrencyDialogItems.isInitialized) {
            //already initialised
            return
        }
        val selectionList = resourceDecoder
                .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.CURRENCIES)
                .toMutableList()
        //add the primary currency to the list
        val primaryCurrency = userSettings.getPrimaryCurrency()
        selectionList.add(index = 0,
                element = SelectionItem(name = primaryCurrency.name, value = primaryCurrency.currency)
        )
        refreshActiveCurrencyForSelectionList(selectionList)
        changeCurrencyDialogItems = selectionList
    }

    private fun initSnapshotDialogOptions() {
        if (::changeSnapshotDialogItems.isInitialized) {
            //already initialised
            return
        }
        val snapshotOptions: List<SelectionItem> = resourceDecoder
                .decodeSelectionItems(desiredSelectionItems = SelectionItemsResource.SNAPSHOTS)
        refreshSelectedSnapshot(selectionList = snapshotOptions)
        changeSnapshotDialogItems = snapshotOptions
    }

    private fun refreshActiveCurrencyForSelectionList(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == coinDisplayController.displayCurrency.currency
        }
    }

    private fun refreshSelectedSnapshot(selectionList: List<SelectionItem>) {
        selectionList.onEach {
            it.isActive = it.value == coinDisplayController.displaySnapshot.stringValue
        }
    }
}