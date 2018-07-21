package com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.model.ParcelableSelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.model.toParcelableSelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.widgets.selectiondialog.model.toSelectionItem
import kotlinx.android.synthetic.main.layout_selection_dialog.view.*
import kotlinx.android.synthetic.main.layout_simple_list_item.view.*
import kotlinx.android.synthetic.main.mergeable_layout_separator.view.*

/**
 * Listener called when a selection is made in the [SelectionDialog]
 */
typealias OnItemSelectedListener = (selectedItem: SelectionItem) -> Unit

typealias ListenerFactory<T> = (T) -> OnItemSelectedListener

/**
 * Dialog capable of displaying a list of items, from which the user can select one.
 *
 * Created by catalin on 21/04/2018.
 */
class SelectionDialog : DialogFragment() {

    private lateinit var data: List<ParcelableSelectionItem>
    private var isDialogCancelable: Boolean = true

    private var selectionListener: OnItemSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getParcelableArrayList<ParcelableSelectionItem>(KEY_DATA).orEmpty()
        isDialogCancelable = arguments?.getBoolean(KEY_CANCELABLE) ?: true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context!!).inflate(R.layout.layout_selection_dialog, null, false)
        val dialog: AlertDialog = AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(isDialogCancelable)
                .create()
        initRecyclerView(view = view)
        initCancelButton(view = view)
        return dialog
    }

    private fun initCancelButton(view: View) {
        val cancelButton = view.dialog_cancel_button
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun setListener(listener: OnItemSelectedListener) {
        this.selectionListener = listener
    }

    private fun initRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.dialog_selection_list
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DialogListAdapter(context = recyclerView.context)
    }

    private inner class DialogListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.text_item_simple_list
        var selectedItemIcon: ImageView = view.image_selected_item_simple_list
        var separator: View = view.view_separator

        init {
            textView.setOnClickListener {
                selectionListener?.invoke(data[adapterPosition].toSelectionItem())
                dismiss()
            }
        }
    }

    private inner class DialogListAdapter(context: Context)
        : RecyclerView.Adapter<DialogListItemViewHolder>() {

        private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListItemViewHolder {
            val view: View = layoutInflater.inflate(R.layout.layout_simple_list_item, parent, false)
            return DialogListItemViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.count()
        }

        override fun onBindViewHolder(holder: DialogListItemViewHolder, position: Int) {
            holder.textView.text = data[position].name
            holder.selectedItemIcon.visibility = if (data[position].activeItem) {
                View.VISIBLE
            } else {
                View.GONE
            }
            if (position == data.size - 1) {
                holder.separator.visibility = View.INVISIBLE
            } else {
                holder.separator.visibility = View.VISIBLE
            }
        }
    }

    open class SelectionDialogIdentifier(val identifier: String)

    class Builder {
        private var selectionListener: OnItemSelectedListener? = null
        private var data: ArrayList<ParcelableSelectionItem>? = null
        private var isCancelable: Boolean = true

        fun data(data: List<SelectionItem>): Builder {
            val parcelableData = ArrayList(data.map {
                it.toParcelableSelectionItem()
            })
            this.data = parcelableData
            return this
        }

        fun selectionListener(selectionListener: OnItemSelectedListener): Builder {
            this.selectionListener = selectionListener
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.isCancelable = cancelable
            return this
        }

        fun build(): SelectionDialog {
            val dialog = SelectionDialog()
            val arguments = Bundle()
            data?.let {
                arguments.putParcelableArrayList(KEY_DATA, data)
                arguments.putBoolean(KEY_CANCELABLE, isCancelable)
            }
            selectionListener?.let {
                dialog.selectionListener = selectionListener
            }
            dialog.arguments = arguments
            return dialog
        }
    }

    companion object {
        private const val KEY_DATA = "SelectionDialog::Arguments::data"
        private const val KEY_CANCELABLE = "SelectionDialog::Arguments::isCancelable"

        fun <T : SelectionDialogIdentifier> showCancelable(dialogIdentifier: T,
                                                           fragmentManager: FragmentManager?,
                                                           data: List<SelectionItem>,
                                                           listenerFactory: ListenerFactory<T>) =
                SelectionDialog.Builder()
                        .selectionListener(selectionListener = listenerFactory.invoke(dialogIdentifier))
                        .data(data = data)
                        .build()
                        .show(fragmentManager, dialogIdentifier.identifier)

        fun <T : SelectionDialogIdentifier> showNonCancelable(dialogIdentifier: T,
                                                              fragmentManager: FragmentManager?,
                                                              data: List<SelectionItem>,
                                                              listenerFactory: ListenerFactory<T>) =
                SelectionDialog.Builder()
                        .selectionListener(selectionListener = listenerFactory.invoke(dialogIdentifier))
                        .data(data = data)
                        .cancelable(cancelable = false)
                        .build()
                        .show(fragmentManager, dialogIdentifier.identifier)

        fun <T : SelectionDialogIdentifier> rebindActiveDialogListeners(fragmentManager: FragmentManager,
                                                                        possibleDialogIdentifiers: Array<T>,
                                                                        listenerFactory: ListenerFactory<T>) {

            val activeFragmentList = possibleDialogIdentifiers
                    .mapNotNull {
                        val fragment = fragmentManager.findFragmentByTag(it.identifier)
                        return@mapNotNull if (fragment != null) {
                            Pair(it, fragment)
                        } else {
                            null
                        }
                    }
                    .apply {
                        if (size > 1) {
                            val activeFragments: String? = map { it.first.identifier }.reduce { acc, s -> "$acc, $s" }
                            throw IllegalStateException("More than 1 selection dialogs present on screen." +
                                    "Active dialogs: $activeFragments")
                        }
                    }
            if (activeFragmentList.isNotEmpty()) {
                activeFragmentList.first()
                        .let {
                            val selectionDialog = it.second as SelectionDialog
                            val listener = listenerFactory.invoke(it.first)
                            selectionDialog.setListener(listener)
                        }
            }
        }
    }
}