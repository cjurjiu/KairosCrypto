package com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.contract

import com.catalinj.cryptosmart.businesslayer.model.PredefinedSnapshot
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem

/**
 * Created by catalinj on 21.01.2018.
 */
interface CoinsDisplayOptionsContract {

    interface CoinsDisplayOptionsPresenter : MvpPresenter<CoinsDisplayOptionsPresenter, CoinsDisplayOptionsView> {
        fun changeCurrencyButtonPressed()

        fun selectSnapshotButtonPressed()

        fun displayCurrencyChanged(newSelectedCurrency: SelectionItem)

        fun selectedSnapshotChanged(newSelectedSnapshot: SelectionItem)
    }

    interface CoinsDisplayOptionsView : MvpView<CoinsDisplayOptionsPresenter, CoinsDisplayOptionsView> {

        fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>)

        fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>)
    }

    interface CoinDisplayController {
        var displayCurrency: CurrencyRepresentation
        var displaySnapshot: PredefinedSnapshot
    }
}