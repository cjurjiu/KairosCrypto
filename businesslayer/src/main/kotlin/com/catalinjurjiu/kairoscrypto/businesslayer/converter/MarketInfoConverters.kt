package com.catalinjurjiu.kairoscrypto.businesslayer.converter

import com.catalinjurjiu.kairoscrypto.businesslayer.model.CryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.database.models.DbCryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinMarketInfo

inline fun DbCryptoCoinMarketInfo.toBusinessLayerMarketInfo(): CryptoCoinMarketInfo {
    return CryptoCoinMarketInfo(rank = this.rank,
            exchangeName = exchangeName,
            exchangePairUrl = exchangePairUrl,
            coinSymbol = coinSymbol,
            exchangePairSymbol1 = exchangePairSymbol1,
            exchangePairSymbol2 = exchangePairSymbol2,
            volumeUsd = volumeUsd,
            priceUsd = priceUsd,
            volumePercent = volumePercent,
            updatedFlag = updatedFlag,
            lastUpdatedTimestamp = lastUpdatedTimestamp)
}

inline fun CryptoCoinMarketInfo.toDataLayerMarketInto(): DbCryptoCoinMarketInfo {
    return DbCryptoCoinMarketInfo(rank = this.rank,
            exchangeName = exchangeName,
            exchangePairUrl = exchangePairUrl,
            coinSymbol = coinSymbol,
            exchangePairSymbol1 = exchangePairSymbol1,
            exchangePairSymbol2 = exchangePairSymbol2,
            volumeUsd = volumeUsd,
            priceUsd = priceUsd,
            volumePercent = volumePercent,
            updatedFlag = updatedFlag,
            lastUpdatedTimestamp = lastUpdatedTimestamp)
}

inline fun CoinMarketCapCryptoCoinMarketInfo.toDataLayerMarketInto(): DbCryptoCoinMarketInfo {
    return DbCryptoCoinMarketInfo(rank = this.rank,
            exchangeName = exchangeName,
            exchangePairUrl = exchangePairUrl,
            coinSymbol = coinSymbol,
            exchangePairSymbol1 = exchangePairSymbol1,
            exchangePairSymbol2 = exchangePairSymbol2,
            volumeUsd = volumeUsd,
            priceUsd = priceUsd,
            volumePercent = volumePercent,
            updatedFlag = updatedFlag,
            lastUpdatedTimestamp = lastUpdatedTimestamp)
}