package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.changeForSnapshot
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.extension.trendlineForPercent
import com.catalinj.cryptosmart.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinj.cryptosmart.presentationlayer.common.view.CryptoListAdapterSettings
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_bookmarks_list_item.view.*

/**
 * Created by catalin on 15/05/2018.
 */
class BookmarksListAdapter(val context: Context,
                           var coins: MutableList<BookmarksCoin>,
                           var adapterSettings: CryptoListAdapterSettings,
                           private val imageHelper: ImageHelper<String>,
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
            val priceData = coinModel.priceData[adapterSettings.currency.currency]!!
            val percentChange = priceData.changeForSnapshot(snapshot = adapterSettings.snapshot)

            imageHelper.setImage(imageCoinLogo, coinModel.symbol)
            textCoinName.text = coinModel.name
            textCoinSymbol.text = coinModel.symbol
            trendLine24H.trendlineForPercent(percent = percentChange)
            textCoinValuePrimary.text = CurrencyFormatter.format(value = priceData.price,
                    currencyRepresentation = adapterSettings.currency)
            textCoinValuePrimaryChange.text = CurrencyFormatter.format(value = percentChange,
                    currencyRepresentation = adapterSettings.currency)

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
        val trendLine24H: AppCompatImageView = v.text_bookmark_coin_trend
    }
}