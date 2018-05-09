package com.catalinj.cryptosmart.businesslayer.model

/**
 * Business layer, source independent, representation of a Crypto Currency.
 */
data class CryptoCoin(val id: String,
                      val rank: Int,
                      val name: String,
                      val symbol: String,
                      val websiteSlug: String,
                      val circulatingSupply: Long,
                      val maxSupply: Long,
                      val lastUpdated: Long,
                      var priceData: PriceData)