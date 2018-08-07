package com.catalinj.cryptosmart.datalayer.database.models

import android.arch.persistence.room.Embedded

/**
 * A [DbCryptoCoin] which contains just one [DbPriceData].
 *
 * Created by catalinj on 27.01.2018.
 */
data class DbCryptoCoinSinglePriceData(@Embedded var cryptoCoin: DbPartialCryptoCoin,
                                       @Embedded var priceData: DbPriceData)