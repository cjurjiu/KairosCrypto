package com.catalinj.cryptosmart.businesslayer.converter

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoinDetails
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin

//Network coin conversions
/**
 * Created by catalinj on 03.02.2018.
 */
fun CoinMarketCapCryptoCoin.toDataLayerCoin(): DbCryptoCoin {
    return DbCryptoCoin(id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            priceUsd = this.priceUsd,
            priceBtc = this.priceBtc,
            volumeUsd24h = this.volumeUsd24h,
            marketCapUsd = this.marketCapUsd,
            availableSupply = this.availableSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            percentChange1h = this.percentChange1h,
            percentChange24h = this.percentChange24h,
            percentChange7d = this.percentChange7d,
            lastUpdated = this.lastUpdated)
}

fun CoinMarketCapCryptoCoin.toDataLayerCoinDetails(): DbCryptoCoinDetails {
    return DbCryptoCoinDetails(id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            priceUsd = this.priceUsd,
            priceBtc = this.priceBtc,
            volumeUsd24h = this.volumeUsd24h,
            marketCapUsd = this.marketCapUsd,
            availableSupply = this.availableSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            percentChange1h = this.percentChange1h,
            percentChange24h = this.percentChange24h,
            percentChange7d = this.percentChange7d,
            lastUpdated = this.lastUpdated)
}

fun CoinMarketCapCryptoCoin.toBusinessLayerCoin(): CryptoCoin {
    return CryptoCoin(id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            priceUsd = this.priceUsd,
            priceBtc = this.priceBtc,
            volumeUsd24h = this.volumeUsd24h,
            marketCapUsd = this.marketCapUsd,
            availableSupply = this.availableSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            percentChange1h = this.percentChange1h,
            percentChange24h = this.percentChange24h,
            percentChange7d = this.percentChange7d,
            lastUpdated = this.lastUpdated)
}

//Db coin conversions
fun DbCryptoCoin.toCoinMarketCapCoin(): CoinMarketCapCryptoCoin {
    return CoinMarketCapCryptoCoin(id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            priceUsd = this.priceUsd,
            priceBtc = this.priceBtc,
            volumeUsd24h = this.volumeUsd24h,
            marketCapUsd = this.marketCapUsd,
            availableSupply = this.availableSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            percentChange1h = this.percentChange1h,
            percentChange24h = this.percentChange24h,
            percentChange7d = this.percentChange7d,
            lastUpdated = this.lastUpdated)
}

fun DbCryptoCoin.toBusinessLayerCoin(): CryptoCoin {
    return CryptoCoin(id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            priceUsd = this.priceUsd,
            priceBtc = this.priceBtc,
            volumeUsd24h = this.volumeUsd24h,
            marketCapUsd = this.marketCapUsd,
            availableSupply = this.availableSupply,
            totalSupply = this.totalSupply,
            maxSupply = this.maxSupply,
            percentChange1h = this.percentChange1h,
            percentChange24h = this.percentChange24h,
            percentChange7d = this.percentChange7d,
            lastUpdated = this.lastUpdated)
}