package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model

import com.google.gson.annotations.SerializedName

/**
 * Model for the details of a crypto currency coin, as returned by the coinmarketcap.com API.
 *
 * Created by catalinj on 27.01.2018.
 */
data class CoinMarketCapCryptoCoinDetails(@SerializedName("id") val id: String,
                                          @SerializedName("name") val name: String,
                                          @SerializedName("symbol") val symbol: String,
                                          @SerializedName("rank") val rank: Int,
                                          @SerializedName("price_usd") val priceUsd: Float,
                                          @SerializedName("price_btc") val priceBtc: Float,
                                          @SerializedName("available_supply") val availableSupply: Long,
                                          @SerializedName("market_cap_usd") val marketCapUsd: Double,
                                          @SerializedName("max_supply") val maxSupply: Long,
                                          @SerializedName("total_supply") val totalSupply: Long,
                                          @SerializedName("24h_volume_usd") val volumeUsd24h: Double,
                                          @SerializedName("percent_change_24h") val percentChange24h: Float,
                                          @SerializedName("percent_change_1h") val percentChange1h: Float,
                                          @SerializedName("percent_change_7d") val percentChange7d: Float,
                                          @SerializedName("last_updated") val lastUpdated: Long)