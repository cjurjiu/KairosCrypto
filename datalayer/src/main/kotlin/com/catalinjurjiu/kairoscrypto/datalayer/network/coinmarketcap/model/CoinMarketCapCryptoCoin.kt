package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model

import com.google.gson.annotations.SerializedName

/**
 * Model for a crypto currency coin, as returned by the coinmarketcap.com API.
 *
 * Created by catalinj on 27.01.2018.
 */
data class CoinMarketCapCryptoCoin(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("symbol") val symbol: String,
        @SerializedName("website_slug") val websiteSlug: String,
        @SerializedName("rank") val rank: Int,
        @SerializedName("circulating_supply") val circulatingSupply: Double,
        @SerializedName("total_supply") val totalSupply: Double,
        @SerializedName("max_supply") val maxSupply: Double,
        @SerializedName("quotes") val quotes: Map<String, Quote>,
        @SerializedName("last_updated") val lastUpdated: Long
)

data class Quote(
        @SerializedName("price") val priceUsd: Float,
        @SerializedName("market_cap") val marketCapUsd: Double,
        @SerializedName("volume_24h") val volumeUsd24h: Double,
        @SerializedName("percent_change_1h") val percentChange1h: Float,
        @SerializedName("percent_change_24h") val percentChange24h: Float,
        @SerializedName("percent_change_7d") val percentChange7d: Float
)