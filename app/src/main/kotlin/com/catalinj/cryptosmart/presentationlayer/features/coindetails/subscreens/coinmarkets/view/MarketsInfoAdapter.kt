package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.extension.displayPercent
import com.catalinj.cryptosmart.presentationlayer.common.formatter.CurrencyFormatter
import kotlinx.android.synthetic.main.layout_coin_markets_list_item.view.*

class MarketsInfoAdapter(var data: List<CryptoCoinMarketInfo>) : RecyclerView.Adapter<MarketInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_coin_markets_list_item, parent, false)
        return MarketInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MarketInfoViewHolder, position: Int) {
        holder.coinRank.text = data[position].rank.toString()
        holder.exchange.text = data[position].exchangeName
        holder.value.text = CurrencyFormatter.format(value = data[position].priceUsd,
                currencyRepresentation = CurrencyRepresentation.USD)
        holder.pair.text = "${data[position].exchangePairSymbol1}/${data[position].exchangePairSymbol2}"
        holder.volumePrc.displayPercent(percent = data[position].volumePercent, colored = false)
        holder.volume.text = CurrencyFormatter.format(value = data[position].volumeUsd,
                currencyRepresentation = CurrencyRepresentation.USD)
    }
}

class MarketInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val coinRank = view.text_markets_coin_rank
    val exchange = view.text_markets_exchange_name
    val value = view.text_markets_price
    val pair = view.text_markets_exchange_pair
    val volumePrc = view.text_markets_volume_prc
    val volume = view.text_markets_volume_24h
}