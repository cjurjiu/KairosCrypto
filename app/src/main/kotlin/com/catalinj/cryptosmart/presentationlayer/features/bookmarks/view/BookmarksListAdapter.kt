package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.example.cryptodrawablesprovider.getCryptoDrawable
import kotlinx.android.synthetic.main.layout_bookmarks_list_item.view.*

/**
 * Created by catalin on 15/05/2018.
 */
class BookmarksListAdapter(val context: Context,
                           var primaryCurrency: CurrencyRepresentation,
                           var coins: MutableList<BookmarksCoin>,
                           private val click: (position: BookmarksCoin) -> Unit) :
        RecyclerView.Adapter<BookmarksListAdapter.BookmarksViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view: View = inflater.inflate(R.layout.layout_bookmarks_list_item, parent, false)
        val viewHolder = BookmarksViewHolder(view)
        viewHolder.apply {
            view.setOnClickListener { click.invoke(coins[this.adapterPosition]) }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        holder.apply {
            val coinModel = coins[position]
            imageCoinLogo.setImageDrawable(
                    getCryptoDrawable(cryptoIdentifier = coinModel.symbol,
                            context = context))
            textCoinName.text = coinModel.name
            textCoinSymbol.text = coinModel.symbol
            textCoinValuePrimary.text = CurrencyFormatter.format(value = coinModel.priceData[primaryCurrency.currency]!!.price,
                    currencyRepresentation = primaryCurrency)
            textCoinValuePrimaryChange.text = CurrencyFormatter.format(value = coinModel.priceData[primaryCurrency.currency]!!.percentChange24h,
                    currencyRepresentation = primaryCurrency)

            if (coinModel.isLoading) {
                imageLoadingBar.visibility = View.VISIBLE
            } else {
                imageLoadingBar.visibility = View.INVISIBLE
            }
        }
    }

    class BookmarksViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        val imageCoinLogo: ImageView = v.image_bookmarks_item_coin_logo
        val textCoinName: TextView = v.text_bookmark_coin_name
        val textCoinSymbol: TextView = v.text_bookmark_coin_symbol
        val textCoinValuePrimary: TextView = v.text_unit_value_primary_currency
        val textCoinValuePrimaryChange: TextView = v.text_percent_value_change_primary_currency
        val imageLoadingBar: View = v.loading_bar
    }
}