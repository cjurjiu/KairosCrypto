package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Business layer, data-layer independent representation of the details for a Crypto Currency.
 */
data class CryptoCoinDetails(val id: String,
                             val rank: Int,
                             val name: String,
                             val symbol: String,
                             val websiteSlug: String,
                             val circulatingSupply: Double,
                             val totalSupply: Double,
                             val maxSupply: Double,
                             var priceData: Map<String, PriceData>,
                             val lastUpdated: String)