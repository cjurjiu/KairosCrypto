package com.catalinj.cryptosmart.repository

import com.catalinj.cryptosmart.datastorage.database.models.DbCryptoCoin
import com.catalinj.cryptosmart.network.coinmarketcap.model.CoinMarketCapCryptoCoin

/**
 * Created by catalinj on 03.02.2018.
 */

fun convertRestCoinToDbCoin(coin: CoinMarketCapCryptoCoin): DbCryptoCoin {
    return DbCryptoCoin(id = coin.id,
            name = coin.name,
            symbol = coin.symbol,
            rank = coin.rank,
            priceUsd = coin.priceUsd,
            priceBtc = coin.priceBtc,
            volumeUsd24h = coin.volumeUsd24h,
            marketCapUsd = coin.marketCapUsd,
            availableSupply = coin.availableSupply,
            totalSupply = coin.totalSupply,
            maxSupply = coin.maxSupply,
            percentChange1h = coin.percentChange1h,
            percentChange24h = coin.percentChange24h,
            percentChange7d = coin.percentChange7d,
            lastUpdated = coin.lastUpdated)
}

fun convertDbCoinToRestCoin(coin: DbCryptoCoin): CoinMarketCapCryptoCoin {
    return CoinMarketCapCryptoCoin(id = coin.id,
            name = coin.name,
            symbol = coin.symbol,
            rank = coin.rank,
            priceUsd = coin.priceUsd,
            priceBtc = coin.priceBtc,
            volumeUsd24h = coin.volumeUsd24h,
            marketCapUsd = coin.marketCapUsd,
            availableSupply = coin.availableSupply,
            totalSupply = coin.totalSupply,
            maxSupply = coin.maxSupply,
            percentChange1h = coin.percentChange1h,
            percentChange24h = coin.percentChange24h,
            percentChange7d = coin.percentChange7d,
            lastUpdated = coin.lastUpdated)
}