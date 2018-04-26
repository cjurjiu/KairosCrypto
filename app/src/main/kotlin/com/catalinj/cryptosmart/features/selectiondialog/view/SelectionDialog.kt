package com.catalinj.cryptosmart.features.selectiondialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.features.selectiondialog.model.ParcelableSelectionItem
import kotlinx.android.synthetic.main.layout_selection_dialog.view.*
import kotlinx.android.synthetic.main.layout_simple_list_item.view.*
import kotlinx.android.synthetic.main.mergeable_layout_separator.view.*

/**
 *
 */
typealias ListenerType = (selectedItem: ParcelableSelectionItem) -> Unit

/**
 *
 * Created by catalin on 21/04/2018.
 */
class SelectionListDialog : DialogFragment() {

    private lateinit var data: List<ParcelableSelectionItem>

    private var listener: ListenerType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getParcelableArrayList<ParcelableSelectionItem>(KEY_DATA).orEmpty()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context!!).inflate(R.layout.layout_selection_dialog, null, false)
        val dialog: AlertDialog = AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(true)
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

    fun setListener(listener: ListenerType) {
        this.listener = listener
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
            textView.setOnClickListener({
                listener?.invoke(data[adapterPosition])
                dismiss()
            })
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

    class Builder {
        private var selectionListener: ListenerType? = null
        private var data: ArrayList<ParcelableSelectionItem>? = null

        fun data(data: ArrayList<ParcelableSelectionItem>): Builder {
            this.data = data
            return this
        }

        fun selectionListener(selectionListener: ListenerType): Builder {
            this.selectionListener = selectionListener
            return this
        }

        fun build(): SelectionListDialog {
            val dialog = SelectionListDialog()
            val arguments = Bundle()
            data?.let {
                arguments.putParcelableArrayList(KEY_DATA, data)
            }
            selectionListener?.let {
                dialog.listener = selectionListener
            }
            dialog.arguments = arguments
            return dialog
        }
    }

    private companion object {
        const val KEY_DATA = "SelectionDialog::Arguments::data"
    }
}