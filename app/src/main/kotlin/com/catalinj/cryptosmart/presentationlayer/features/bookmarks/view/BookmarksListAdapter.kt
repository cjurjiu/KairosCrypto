package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.example.cryptodrawablesprovider.getCryptoDrawable
import kotlinx.android.synthetic.main.layout_bookmarks_list_item.view.*

/**
 * Created by catalin on 15/05/2018.
 */
class BookmarksListAdapter(val context: Context,
                           var primaryCurrency: CurrencyRepresentation,
                           var coins: List<CryptoCoin>,
                           private val click: (position: CryptoCoin) -> Unit) :
        RecyclerView.Adapter<BookmarksListAdapter.BookmarksViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view: View = inflater.inflate(R.layout.layout_bookmarks_list_item, parent, false)
        val viewHolder = BookmarksViewHolder(view)
        viewHolder.apply { view.setOnClickListener { click.invoke(coins[this.adapterPosition]) } }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        holder.apply {
            imageCoinLogo.setImageDrawable(
                    getCryptoDrawable(cryptoIdentifier = coins[position].symbol,
                            context = context))
            textCoinName.text = coins[position].name
            textCoinSymbol.text = coins[position].symbol
            textCoinValuePrimary.text = coins[position].priceData[primaryCurrency.currency]?.price.toString()
            textCoinValuePrimaryChange.text = coins[position].priceData[primaryCurrency.currency]?.percentChange24h.toString()
            textCoinValueBtc.text = coins[position].priceData[CurrencyRepresentation.BTC.currency]?.price.toString()
            textCoinValueBitcoinChange.text = coins[position].priceData[CurrencyRepresentation.BTC.currency]?.percentChange24h.toString()
        }
    }

    class BookmarksViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        val imageCoinLogo: ImageView = v.image_bookmarks_item_coin_logo
        val textCoinName: TextView = v.text_bookmark_coin_name
        val textCoinSymbol: TextView = v.text_bookmark_coin_symbol
        val textCoinValuePrimary: TextView = v.text_unit_value_primary_currency
        val textCoinValuePrimaryChange: TextView = v.text_percent_value_change_primary_currency
        val textCoinValueBtc: TextView = v.text_unit_value_btc
        val textCoinValueBitcoinChange: TextView = v.text_percent_value_change_btc
    }
}