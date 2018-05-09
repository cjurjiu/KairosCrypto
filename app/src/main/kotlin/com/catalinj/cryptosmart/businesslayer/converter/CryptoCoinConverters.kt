package com.catalinj.cryptosmart.businesslayer.converter

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.model.PriceData
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbPartialCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbPriceData
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails

/**
 * Created by catalinj on 03.02.2018.
 */
//Crypto coin conversions

//Network model conversions
fun CoinMarketCapCryptoCoin.toDataLayerCoin(): DbPartialCryptoCoin {
    return DbPartialCryptoCoin(serverId = this.id,
            rank = this.rank,
            name = this.name,
            symbol = this.symbol,
            websiteSlug = this.websiteSlug,
            circulatingSupply = this.circulatingSupply,
            maxSupply = this.maxSupply,
            lastUpdated = this.lastUpdated
    )
}

fun CoinMarketCapCryptoCoin.toDataLayerPriceData(): List<DbPriceData> {
    return quotes.map {
        val capCryptoCoin = this@toDataLayerPriceData
        val currency = it.key
        val priceInfo = it.value

        DbPriceData(coinSymbol = capCryptoCoin.symbol,
                currency = currency,
                coinServerId = capCryptoCoin.id,
                price = priceInfo.priceUsd,
                marketCap = priceInfo.marketCapUsd,
                volume24h = priceInfo.volumeUsd24h,
                percentChange1h = priceInfo.percentChange1h,
                percentChange24h = priceInfo.percentChange24h,
                percentChange7d = priceInfo.percentChange7d,
                lastUpdated = capCryptoCoin.lastUpdated
        )
    }
}

fun CoinMarketCapCryptoCoinDetails.toDataLayerPriceData(): List<DbPriceData> {
    return data.quotes.map {
        val capCryptoCoin = this@toDataLayerPriceData.data
        val currency = it.key
        val priceInfo = it.value

        DbPriceData(coinSymbol = capCryptoCoin.symbol,
                currency = currency,
                coinServerId = capCryptoCoin.id,
                price = priceInfo.priceUsd,
                marketCap = priceInfo.marketCapUsd,
                volume24h = priceInfo.volumeUsd24h,
                percentChange1h = priceInfo.percentChange1h,
                percentChange24h = priceInfo.percentChange24h,
                percentChange7d = priceInfo.percentChange7d,
                lastUpdated = capCryptoCoin.lastUpdated
        )
    }
}

fun DbPartialCryptoCoin.toBusinessLayerCoinDetails(): CryptoCoinDetails {
    return CryptoCoinDetails(id = this.serverId,
            name = this.name,
            symbol = this.symbol,
            websiteSlug = this.websiteSlug,
            rank = this.rank,
            circulatingSupply = this.circulatingSupply,
            maxSupply = this.maxSupply,
            lastUpdated = this.lastUpdated,
            priceData = EmptyPriceData
    )
}

fun DbCryptoCoin.toBusinessLayerCoin(): CryptoCoin {
    val dbPriceData = this.priceData
    val dbCoin = this.cryptoCoin

    val priceData = PriceData(
            currency = dbPriceData.currency,
            price = dbPriceData.price,
            marketCap = dbPriceData.marketCap,
            volume24h = dbPriceData.volume24h,
            percentChange1h = dbPriceData.percentChange1h,
            percentChange24h = dbPriceData.percentChange24h,
            percentChange7d = dbPriceData.percentChange7d,
            lastUpdated = dbPriceData.lastUpdated)

    return CryptoCoin(
            id = dbCoin.serverId,
            rank = dbCoin.rank,
            name = dbCoin.name,
            symbol = dbCoin.symbol,
            websiteSlug = dbCoin.websiteSlug,
            circulatingSupply = dbCoin.circulatingSupply,
            maxSupply = dbCoin.maxSupply,
            lastUpdated = dbCoin.lastUpdated,
            priceData = priceData)
}

fun DbCryptoCoin.toBusinessLayerCoinDetails(): CryptoCoinDetails {
    val dbPriceData = this.priceData
    val dbCoin = this.cryptoCoin

    val priceData = PriceData(
            currency = dbPriceData.currency,
            price = dbPriceData.price,
            marketCap = dbPriceData.marketCap,
            volume24h = dbPriceData.volume24h,
            percentChange1h = dbPriceData.percentChange1h,
            percentChange24h = dbPriceData.percentChange24h,
            percentChange7d = dbPriceData.percentChange7d,
            lastUpdated = dbPriceData.lastUpdated)

    return CryptoCoinDetails(
            id = dbCoin.serverId,
            rank = dbCoin.rank,
            name = dbCoin.name,
            symbol = dbCoin.symbol,
            websiteSlug = dbCoin.websiteSlug,
            circulatingSupply = dbCoin.circulatingSupply,
            maxSupply = dbCoin.maxSupply,
            lastUpdated = dbCoin.lastUpdated,
            priceData = priceData)
}

private val EmptyPriceData = PriceData(currency = "",
        price = 0F,
        marketCap = 0.0,
        volume24h = 0.0,
        percentChange1h = 0F,
        percentChange24h = 0F,
        percentChange7d = 0F,
        lastUpdated = 0L)