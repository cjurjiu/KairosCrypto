package com.catalinj.cryptosmart.presentationlayer.common.converter

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.presentationlayer.common.model.ParcelableCryptoCoin

fun CryptoCoin.toParcelable(cryptoCoin: CryptoCoin): ParcelableCryptoCoin {
    return ParcelableCryptoCoin(id = cryptoCoin.id,
            name = cryptoCoin.name,
            symbol = cryptoCoin.symbol,
            rank = cryptoCoin.rank,
            priceUsd = cryptoCoin.priceUsd,
            priceBtc = cryptoCoin.priceBtc,
            availableSupply = cryptoCoin.availableSupply,
            marketCapUsd = cryptoCoin.marketCapUsd,
            maxSupply = cryptoCoin.maxSupply,
            totalSupply = cryptoCoin.totalSupply,
            volumeUsd24h = cryptoCoin.volumeUsd24h,
            percentChange24h = cryptoCoin.percentChange24h,
            percentChange1h = cryptoCoin.percentChange1h,
            percentChange7d = cryptoCoin.percentChange7d,
            lastUpdated = cryptoCoin.lastUpdated)
}

fun ParcelableCryptoCoin.toCryptoCoin(): CryptoCoin {
    return CryptoCoin(id = id,
            name = name,
            symbol = symbol,
            rank = rank,
            priceUsd = priceUsd,
            priceBtc = priceBtc,
            availableSupply = availableSupply,
            marketCapUsd = marketCapUsd,
            maxSupply = maxSupply,
            totalSupply = totalSupply,
            volumeUsd24h = volumeUsd24h,
            percentChange24h = percentChange24h,
            percentChange1h = percentChange1h,
            percentChange7d = percentChange7d,
            lastUpdated = lastUpdated)
}