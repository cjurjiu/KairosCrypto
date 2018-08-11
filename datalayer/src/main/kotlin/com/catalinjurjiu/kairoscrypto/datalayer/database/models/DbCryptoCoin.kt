package com.catalinjurjiu.kairoscrypto.datalayer.database.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by catalinj on 27.01.2018.
 */
data class DbCryptoCoin @JvmOverloads constructor(@Embedded var cryptoCoin: DbPartialCryptoCoin,
                                                  @Relation(entity = DbPriceData::class,
                                                          parentColumn = DbPartialCryptoCoin.ColumnNames.SYMBOL,
                                                          entityColumn = DbPriceData.ColumnNames.COIN_SYMBOL)
                                                  var priceData: List<DbPriceData> = emptyList())