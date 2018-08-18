package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Business layer, data-layer independent representation of an object which stores the value of
 * Cryptocurrency in one other currency (typically FIAT, but can also be Bitcoin).
 */
data class PriceData(val currency: String,
                     val price: Float,
                     val marketCap: Double,
                     val volume24h: Double,
                     val percentChange1h: Float,
                     val percentChange24h: Float,
                     val percentChange7d: Float,
                     val lastUpdated: Long = -1)