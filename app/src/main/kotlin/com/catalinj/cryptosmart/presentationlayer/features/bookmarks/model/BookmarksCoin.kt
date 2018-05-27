package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.PriceData

data class BookmarksCoin(val id: String,
                         val rank: Int,
                         val name: String,
                         val symbol: String,
                         val websiteSlug: String,
                         val circulatingSupply: Double,
                         val totalSupply: Double,
                         val maxSupply: Double,
                         var priceData: Map<String, PriceData>,
                         val lastUpdated: Long,
                         val isLoading: Boolean)

inline fun CryptoCoin.toBookmarksCoin(isLoading: Boolean = false): BookmarksCoin {
//    todo, remove the random once actual behavior is being added
//    val r = Random()

    return BookmarksCoin(id = this.id,
            rank = this.rank,
            name = this.name,
            symbol = this.symbol,
            websiteSlug = this.websiteSlug,
            circulatingSupply = this.circulatingSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            priceData = this.priceData,
            lastUpdated = this.lastUpdated,
            isLoading = isLoading)
//            isLoading = r.nextBoolean())
}

inline fun BookmarksCoin.toBusinessLayerCoin(): CryptoCoin {
    return CryptoCoin(id = this.id,
            rank = this.rank,
            name = this.name,
            symbol = this.symbol,
            websiteSlug = this.websiteSlug,
            circulatingSupply = this.circulatingSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            priceData = this.priceData,
            lastUpdated = this.lastUpdated)
}