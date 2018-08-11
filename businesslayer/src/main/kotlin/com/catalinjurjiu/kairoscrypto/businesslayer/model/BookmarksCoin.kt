package com.catalinjurjiu.kairoscrypto.businesslayer.model

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