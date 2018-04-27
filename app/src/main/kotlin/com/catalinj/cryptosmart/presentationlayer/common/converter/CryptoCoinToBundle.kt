package com.catalinj.cryptosmart.presentationlayer.common.converter

import android.os.Bundle
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin

private const val KEY_COIN_ID = "ARG::COIN_ID"
private const val KEY_COIN_NAME = "ARG::COIN_NAME"
private const val KEY_COIN_SYMBOL = "ARG::COIN_SYMBOL"
private const val NO_VALUE = "COIN_NO_VALUE"
private const val NO_VALUE_INT = -1337

fun CryptoCoin.toBundle(): Bundle {
    val bundle = Bundle()
    bundle.putString(KEY_COIN_ID, this.id)
    bundle.putString(KEY_COIN_NAME, this.name)
    bundle.putString(KEY_COIN_SYMBOL, this.symbol)
    return bundle
}

fun Bundle.getCryptoCoin(): CryptoCoin {
    val id: String = getString(KEY_COIN_ID)
    val name: String = getString(KEY_COIN_NAME)
    val symbol: String = getString(KEY_COIN_SYMBOL)
    return CryptoCoin(id = id,
            name = name,
            symbol = symbol,
            rank = NO_VALUE_INT,
            priceUsd = NO_VALUE_INT.toFloat(),
            priceBtc = NO_VALUE_INT.toFloat(),
            availableSupply = NO_VALUE_INT.toLong(),
            marketCapUsd = NO_VALUE_INT.toDouble(),
            maxSupply = NO_VALUE_INT.toLong(),
            totalSupply = NO_VALUE_INT.toLong(),
            volumeUsd24h = NO_VALUE_INT.toDouble(),
            percentChange24h = NO_VALUE_INT.toFloat(),
            percentChange1h = NO_VALUE_INT.toFloat(),
            percentChange7d = NO_VALUE_INT.toFloat(),
            lastUpdated = NO_VALUE_INT.toLong())
}