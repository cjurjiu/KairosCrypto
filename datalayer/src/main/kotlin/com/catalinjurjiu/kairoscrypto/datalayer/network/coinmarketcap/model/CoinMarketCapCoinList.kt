package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model

import com.google.gson.annotations.SerializedName


/**
 * Model for a crypto currency coin, as returned by the coinmarketcap.com API.
 *
 * Created by catalinj on 27.01.2018.
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