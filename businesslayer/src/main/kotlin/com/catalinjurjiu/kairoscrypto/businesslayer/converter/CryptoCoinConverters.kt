package com.catalinjurjiu.kairoscrypto.businesslayer.converter

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinDetails
import com.catalinjurjiu.kairoscrypto.businesslayer.model.PriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinSinglePriceData
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPartialCryptoCoin
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbPriceData
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin

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
            totalSupply = this.totalSupply,
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

fun DbCryptoCoin.toBusinessLayerCoin(): CryptoCoin {
    val dbPriceData = this.priceData
    val dbCoin = this.cryptoCoin

    val businessLayerPriceDataMap: MutableMap<String, PriceData> = mutableMapOf()

    dbPriceData.forEach {
        businessLayerPriceDataMap[it.currency] = PriceData(
                currency = it.currency,
                price = it.price,
                marketCap = it.marketCap,
                volume24h = it.volume24h,
                percentChange1h = it.percentChange1h,
                percentChange24h = it.percentChange24h,
                percentChange7d = it.percentChange7d,
                lastUpdated = it.lastUpdated)
    }

    return CryptoCoin(
            id = dbCoin.serverId,
            rank = dbCoin.rank,
            name = dbCoin.name,
            symbol = dbCoin.symbol,
            websiteSlug = dbCoin.websiteSlug,
            circulatingSupply = dbCoin.circulatingSupply,
            totalSupply = dbCoin.totalSupply,
            maxSupply = dbCoin.maxSupply,
            priceData = businessLayerPriceDataMap,
            lastUpdated = dbCoin.lastUpdated)
}

fun DbCryptoCoin.toBusinessLayerCoinDetails(): CryptoCoinDetails {
    val coin = this.toBusinessLayerCoin()
    return CryptoCoinDetails(id = coin.id,
            rank = coin.rank,
            name = coin.name,
            symbol = coin.symbol,
            websiteSlug = coin.websiteSlug,
            circulatingSupply = coin.circulatingSupply,
            totalSupply = coin.totalSupply,
            maxSupply = coin.maxSupply,
            priceData = coin.priceData,
            lastUpdated = coin.lastUpdated)
}

fun DbCryptoCoinSinglePriceData.toBusinessLayerCoin(): CryptoCoin {
    val dbPriceData = this.priceData
    val dbCoin = this.cryptoCoin
    val businessLayerPriceDataMap: MutableMap<String, PriceData> = mutableMapOf()

    businessLayerPriceDataMap[dbPriceData.currency] = PriceData(
            currency = dbPriceData.currency,
            price = dbPriceData.price,
            marketCap = dbPriceData.marketCap,
            volume24h = dbPriceData.volume24h,
            percentChange1h = dbPriceData.percentChange1h,
            percentChange24h = dbPriceData.percentChange24h,
            percentChange7d = dbPriceData.percentChange7d,
            lastUpdated = dbPriceData.lastUpdated)

    return CryptoCoin(id = dbCoin.serverId,
            rank = dbCoin.rank,
            name = dbCoin.name,
            symbol = dbCoin.symbol,
            websiteSlug = dbCoin.websiteSlug,
            circulatingSupply = dbCoin.circulatingSupply,
            totalSupply = dbCoin.totalSupply,
            maxSupply = dbCoin.maxSupply,
            priceData = businessLayerPriceDataMap,
            lastUpdated = dbCoin.lastUpdated)
}

private val EmptyPriceData = PriceData(
        currency = "",
        price = 0F,
        marketCap = 0.0,
        volume24h = 0.0,
        percentChange1h = 0F,
        percentChange24h = 0F,
        percentChange7d = 0F,
        lastUpdated = "")