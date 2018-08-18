package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Business layer, data-layer independent representation of a Market (exchange) on which a
 * Crypto Currency is traded.
 */
data class CryptoCoinMarketInfo(val rank: Int,
                                val exchangeName: String,
                                val exchangePairUrl: String,
                                val coinSymbol: String,
                                val exchangePairSymbol1: String,
                                val exchangePairSymbol2: String,
                                val volumeUsd: Double,
                                val priceUsd: Double,
                                val volumePercent: Float,
                                val updatedFlag: String,
                                val lastUpdatedTimestamp: Long)