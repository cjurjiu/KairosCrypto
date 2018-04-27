package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.example.cryptodrawablesprovider.getCryptoDrawable
import kotlinx.android.synthetic.main.layout_coin_list_item.view.*

/**
 * Created by catalinj on 03.02.2018.
 */
class CoinListAdapter(context: Context,
                      var coins: List<CryptoCoin>,
                      private val click: (position: CryptoCoin) -> Unit) :
        RecyclerView.Adapter<CoinListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = inflater.inflate(R.layout.layout_coin_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            view.setOnClickListener { click.invoke(coins[position]) }
            textCoinRank.text = coins[position].rank.toString()
            imageCoinLogo.setImageDrawable(getCryptoDrawable(cryptoIdentifier = coins[position].symbol,
                    context = holder.itemView.context))
            textCoinName.text = coins[position].name
            textCoinValue.text = "\$${coins[position].priceUsd} USD"
            textCoinIncreasePrc.text = "${coins[position].percentChange24h}%"
            textCoinIncreaseValue.text = "\$${(coins[position].percentChange24h * coins[position].priceUsd / 100f)} USD"
        }
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        val textCoinRank: TextView = v.text_coin_rank
        val imageCoinLogo: ImageView = v.image_coin_logo
        val textCoinName: TextView = v.text_coin_name
        val textCoinValue: TextView = v.text_coin_value
        val textCoinIncreasePrc: TextView = v.text_coin_increase_percent
        val textCoinIncreaseValue: TextView = v.text_coin_increase_value
    }
}