package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Represents one entry in the markets table
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