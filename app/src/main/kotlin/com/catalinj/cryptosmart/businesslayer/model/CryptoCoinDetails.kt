package com.catalinj.cryptosmart.businesslayer.model

/**
 * Business layer, source independent, representation of the details for a Crypto Currency.
 */
data class CryptoCoinDetails(val id: String,
                             val name: String,
                             val symbol: String,
                             val rank: Int,
                             val priceUsd: Float,
                             val priceBtc: Float,
                             val availableSupply: Long,
                             val marketCapUsd: Double,
                             val maxSupply: Long,
                             val totalSupply: Long,
                             val volumeUsd24h: Double,
                             val percentChange24h: Float,
                             val percentChange1h: Float,
                             val percentChange7d: Float,
                             val lastUpdated: Long)