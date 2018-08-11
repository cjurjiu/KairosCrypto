package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.view

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.PredefinedSnapshot
import com.catalinjurjiu.kairoscrypto.businesslayer.model.changeForSnapshot
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.percentAsAbsoluteChange
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension.trendlineForPercent
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.CryptoListAdapterSettings
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
            textCoinValuePrimaryChange.percentAsAbsoluteChange(initialValue = priceData.price,
                    percentChange = percentChange,
                    currency = adapterSettings.currency)
//            textCoinValuePrimaryChange.displayAsPercent(initialValue = priceData.price, updateValue = percentChange)
            textUnitTimePeriodChange.setText(getStringForSnapshot(snapshot = adapterSettings.snapshot))

            if (coinModel.isLoading) {
                imageLoadingBar.visibility = View.VISIBLE
            } else {
                imageLoadingBar.visibility = View.INVISIBLE
            }
        }
    }

    @StringRes
    private fun getStringForSnapshot(snapshot: PredefinedSnapshot): Int {
        return when (snapshot) {
            PredefinedSnapshot.SNAPSHOT_1H -> R.string.change_over_past_hour
            PredefinedSnapshot.SNAPSHOT_24H -> R.string.change_over_24_hours
            PredefinedSnapshot.SNAPSHOT_7D -> R.string.change_over_7_days
        }
    }

    class BookmarksViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        val imageCoinLogo: ImageView = v.image_bookmarks_item_coin_logo
        val textCoinName: TextView = v.text_bookmark_coin_name
        val textCoinSymbol: TextView = v.text_bookmark_coin_symbol
        val textCoinValuePrimary: TextView = v.text_unit_value_primary_currency
        val textCoinValuePrimaryChange: TextView = v.text_percent_value_change_primary_currency
        val textUnitTimePeriodChange: TextView = v.text_unit_time_period_change
        val imageLoadingBar: View = v.loading_bar
        val trendLine24H: AppCompatImageView = v.text_bookmark_coin_trend
    }
}