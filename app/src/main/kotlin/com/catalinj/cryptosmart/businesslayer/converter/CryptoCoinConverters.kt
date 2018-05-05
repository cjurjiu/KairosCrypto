package com.catalinj.cryptosmart.businesslayer.converter

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.datalayer.database.coindetails.DbCryptoCoinDetails
import com.catalinj.cryptosmart.datalayer.database.coins.DbCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoin
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinDetails

/**
 * Created by catalinj on 03.02.2018.
 */
//Crypto coin conversions

//Network model conversions
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

//coin details conversions

//network model conversions
fun CoinMarketCapCryptoCoinDetails.toDataLayerCoinDetails(): DbCryptoCoinDetails {
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

fun CoinMarketCapCryptoCoinDetails.toBusinessLayerCoinDetails(): CryptoCoinDetails {
    return CryptoCoinDetails(id = this.id,
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

//db model conversions
fun DbCryptoCoinDetails.toCoinMarketCapCoin(): CoinMarketCapCryptoCoinDetails {
    return CoinMarketCapCryptoCoinDetails(id = this.id,
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

fun DbCryptoCoinDetails.toBusinessLayerCoinDetails(): CryptoCoinDetails {
    return CryptoCoinDetails(id = this.id,
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