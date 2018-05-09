package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model

import com.google.gson.annotations.SerializedName

/**
 * Created by catalin on 07/05/2018.
 */
data class CoinMarketCapCryptoCoinListResponse(
        @SerializedName("data") val data: Map<String, CoinMarketCapCryptoCoin>,
        @SerializedName("metadata") val metadata: CoinMarketCapCryptoCoinListMetadata
)

data class CoinMarketCapCryptoCoinListMetadata(
        @SerializedName("timestamp") val timestamp: Long,
        @SerializedName("num_cryptocurrencies") val numCryptocurrencies: Int,
        @SerializedName("error") val error: String
)