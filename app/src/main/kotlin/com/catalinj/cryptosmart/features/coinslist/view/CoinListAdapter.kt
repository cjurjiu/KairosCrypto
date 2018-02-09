package com.catalinj.cryptosmart.features.coinslist.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.network.CoinMarketCapCryptoCoin
import kotlinx.android.synthetic.main.layout_list_item.view.*

/**
 * Created by catalinj on 03.02.2018.
 */
class CoinListAdapter(context: Context,
                      var coins: List<CoinMarketCapCryptoCoin>,
                      private val click: () -> Unit) :
        RecyclerView.Adapter<CoinListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view: View = inflater.inflate(R.layout.layout_list_item, parent, false)
        view.setOnClickListener { click.invoke() }
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder!!.textCoinName.text = coins[position].name
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textCoinName: TextView = v.text_coin_name
    }
}