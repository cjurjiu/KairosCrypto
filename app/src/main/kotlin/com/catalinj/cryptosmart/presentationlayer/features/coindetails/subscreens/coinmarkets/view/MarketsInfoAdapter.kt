package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
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
        holder.coinName.text = data[position].coinSymbol
        holder.exchange.text = data[position].exchangeName
        holder.value.text = data[position].priceUsd.toString()
        holder.pair.text = "${data[position].exchangePairSymbol1}/${data[position].exchangePairSymbol2}"
        holder.volumePrc.text = data[position].volumePercent.toString()
//        exchanges are missing. rank 1 jumps to rank 4 or so...inspect what might be the cause
//        issue might be related to the uniqueness of the primary key in the db and to the insert
//        /override strategy
    }
}

class MarketInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val coinRank = view.text_markets_coin_rank
    val coinName = view.text_markets_coin_name
    val exchange = view.text_markets_exchange_name
    val value = view.text_markets_price
    val pair = view.text_markets_exchange_pair
    val volumePrc = view.text_markets_volume_prc
}