package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Models one Crypto Currency coin.
 *
 * Composite object composed of:
 * * one [DbPartialCryptoCoin], which stores metadata about the coin.
 * * one or more [DbPriceData] objects which store the value of this coin. Each [DbPriceData] stores
 * the value of this coin in one particular currency. Always at least one item.
 *
 * @property cryptoCoin Metadata about the coin.
 * @property priceData list of [DbPriceData] objects which store the value of this coin. Each [DbPriceData] stores
 * the value of this coin in one particular currency. Always at least one item.
 *
 * @see DbCryptoCoinSinglePriceData
 *
 * Created by catalinj on 27.01.2018.
 */
data class DbCryptoCoin @JvmOverloads constructor(@Embedded var cryptoCoin: DbPartialCryptoCoin,
                                                  @Relation(entity = DbPriceData::class,
                                                          parentColumn = DbPartialCryptoCoin.ColumnNames.SYMBOL,
                                                          entityColumn = DbPriceData.ColumnNames.COIN_SYMBOL)
                                                  var priceData: List<DbPriceData> = emptyList())