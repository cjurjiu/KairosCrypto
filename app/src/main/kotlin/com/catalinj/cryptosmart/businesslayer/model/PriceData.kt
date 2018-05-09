package com.catalinj.cryptosmart.businesslayer.model

/**
 * Created by catalin on 08/05/2018.
 */
data class PriceData(val currency: String,
                     val price: Float,
                     val marketCap: Double,
                     val volume24h: Double,
                     val percentChange1h: Float,
                     val percentChange24h: Float,
                     val percentChange7d: Float,
                     val lastUpdated: Long = -1)