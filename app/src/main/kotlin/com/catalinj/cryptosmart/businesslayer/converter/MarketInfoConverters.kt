package com.catalinj.cryptosmart.businesslayer.converter

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinMarketInfo
import com.catalinj.cryptosmart.datalayer.database.models.DbCryptoCoinMarketInfo

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