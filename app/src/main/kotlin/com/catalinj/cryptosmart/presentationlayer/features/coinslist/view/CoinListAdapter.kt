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
import com.catalinj.cryptosmart.businesslayer.model.changeForSnapshot
import com.catalinj.cryptosmart.presentationlayer.common.extension.displayPercent
import com.catalinj.cryptosmart.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinj.cryptosmart.presentationlayer.common.view.CryptoListAdapterSettings
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_coin_list_item.view.*

/**
 * Created by catalinj on 03.02.2018.
 */
class CoinListAdapter(context: Context,
                      var coins: List<CryptoCoin>,
                      var adapterSettings: CryptoListAdapterSettings,
                      private val imageHelper: ImageHelper<String>,
                      private val click: (position: CryptoCoin) -> Unit) :
        RecyclerView.Adapter<CoinListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = inflater.inflate(R.layout.layout_coin_list_item, parent, false)
        val viewHolder = MyViewHolder(view)
        viewHolder.apply { view.setOnClickListener { click.invoke(coins[this.adapterPosition]) } }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val coin = coins[position]
            val priceData = coin.priceData[adapterSettings.currency.currency]!!
            val percentChange = priceData.changeForSnapshot(snapshot = adapterSettings.snapshot)

            imageHelper.setImage(imageCoinLogo, coin.symbol)
            textCoinRank.text = coin.rank.toString()
            textCoinName.text = coin.name
            textCoinValue.text = CurrencyFormatter.format(value = priceData.price,
                    currencyRepresentation = adapterSettings.currency)
            textCoinIncreasePrc.displayPercent(percent = percentChange)
            val value = (percentChange * priceData.price) / 100f
            textCoinIncreaseValue.text = CurrencyFormatter.format(value = value,
                    currencyRepresentation = adapterSettings.currency)
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