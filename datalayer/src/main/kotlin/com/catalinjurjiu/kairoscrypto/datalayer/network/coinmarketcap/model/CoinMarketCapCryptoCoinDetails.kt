package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model

import com.google.gson.annotations.SerializedName

/**
 * Model for the details of a crypto currency coin, as returned by the coinmarketcap.com API.
 *
 * Created by catalinj on 27.01.2018.
 */
data class CoinMarketCapCryptoCoinDetails(
        @SerializedName("data") val data:  Map<String, CoinMarketCapCryptoCoin>,
        @SerializedName("status") val metadata: CoinMarketCapCryptoCoinDetailsMetadata
)

data class CoinMarketCapCryptoCoinDetailsMetadata(
        @SerializedName("timestamp") val timestamp: String,
        @SerializedName("errorCode") val errorCode: Int,
        @SerializedName("errorMessage") val errorMessage: String
)