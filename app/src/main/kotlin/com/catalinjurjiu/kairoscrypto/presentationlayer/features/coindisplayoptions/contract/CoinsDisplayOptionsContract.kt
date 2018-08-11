package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.PredefinedSnapshot
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.model.SelectionItem

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