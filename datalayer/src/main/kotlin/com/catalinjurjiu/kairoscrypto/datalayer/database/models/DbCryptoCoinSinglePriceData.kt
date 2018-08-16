package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.Embedded

/**
 * Convenience data model which represents one Crypto Currency coin which has its value represented
 * by exactly one [DbPriceData].
 *
 * Composite object composed of:
 * * one [DbPartialCryptoCoin], which stores metadata about the coin.
 * * **exactly** one [DbPriceData] object which stores the value of this coin in exactly one currency.
 *
 * @property cryptoCoin Metadata about the coin.
 * @property priceData [DbPriceData] object which stores the value of this coin in a particular
 * currency.
 *
 * Use [DbCryptoCoin] to retrieve a Coin model which has its value represented in several currencies.
 *
 * @see DbCryptoCoinSinglePriceData
 *
 * Created by catalinj on 27.01.2018.
 */
data class DbCryptoCoinSinglePriceData(@Embedded var cryptoCoin: DbPartialCryptoCoin,
                                       @Embedded var priceData: DbPriceData)