package com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.view

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.ImageButton
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.getSupportActivity
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coinslist.view.CoinListSelectionDialogType
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.model.SelectionItem
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.view.OnItemSelectedListener
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.view.SelectionDialog
import javax.inject.Inject

class CoinDisplayOptionsToolbar @JvmOverloads constructor(context: Context?,
                                                          attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0) :
        Toolbar(context, attrs, defStyleAttr),
        CoinsDisplayOptionsContract.CoinsDisplayOptionsView,
        DefaultLifecycleObserver {

    private val fragmentManager: FragmentManager

    @Inject
    protected lateinit var coinDisplayOptionsPresenter: CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter

    init {
        val activity = this.getSupportActivity()
        if (activity != null) {
            fragmentManager = activity.supportFragmentManager
        } else {
            throw IllegalStateException("CoinDisplayOptionsToolbar needs to be attached to a FragmentActivity.")
        }
    }

    //lifecycle methods
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        coinDisplayOptionsPresenter.startPresenting()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        coinDisplayOptionsPresenter.stopPresenting()
    }
    //end lifecycle methods

    //mvp view methods
    override fun initialise() {
        //set the change currency button clicked listener
        val changeCurrencyButton = this.findViewById<ImageButton>(R.id.button_change_currency)
        changeCurrencyButton?.setOnClickListener { coinDisplayOptionsPresenter.changeCurrencyButtonPressed() }
        //set the change currency button clicked listener
        val selectSnapshotButton = this.findViewById<ImageButton>(R.id.button_snapshots)
        selectSnapshotButton?.setOnClickListener { coinDisplayOptionsPresenter.selectSnapshotButtonPressed() }
        //rebind here?
        SelectionDialog.rebindActiveDialogListeners(fragmentManager = fragmentManager,
                possibleDialogIdentifiers = CoinListSelectionDialogType.children(),
                listenerFactory = ::getListenerForDialogType)
    }

    override fun getPresenter(): CoinsDisplayOptionsContract.CoinsDisplayOptionsPresenter = coinDisplayOptionsPresenter
    //end mvp view methods

    //display options toolbar methods
    override fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>) {
        SelectionDialog.showCancelable(dialogIdentifier = CoinListSelectionDialogType.ChangeCurrency,
                fragmentManager = fragmentManager,
                title = context.getString(R.string.dialog_pick_currency),
                data = selectionItems,
                listenerFactory = ::getListenerForDialogType)
    }

    override fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>) {
        SelectionDialog.showCancelable(dialogIdentifier = CoinListSelectionDialogType.SelectSnapshot,
                fragmentManager = fragmentManager,
                title = context.getString(R.string.dialog_pick_period),
                data = selectionItems,
                listenerFactory = ::getListenerForDialogType)
    }
    //ene display options toolbar methods

    private fun getListenerForDialogType(dialogType: CoinListSelectionDialogType): OnItemSelectedListener {
        return when (dialogType) {
            CoinListSelectionDialogType.ChangeCurrency ->
                { item -> coinDisplayOptionsPresenter.displayCurrencyChanged(item) }
            CoinListSelectionDialogType.SelectSnapshot ->
                { item -> coinDisplayOptionsPresenter.selectedSnapshotChanged(item) }
        }
    }
}