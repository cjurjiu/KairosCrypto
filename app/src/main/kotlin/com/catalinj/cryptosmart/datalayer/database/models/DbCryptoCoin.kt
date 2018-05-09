package com.catalinj.cryptosmart.datalayer.database.models

import android.arch.persistence.room.Embedded

/**
 * Created by catalinj on 27.01.2018.
 */
data class DbCryptoCoin(@Embedded var cryptoCoin: DbPartialCryptoCoin,
                        @Embedded var priceData: DbPriceData)