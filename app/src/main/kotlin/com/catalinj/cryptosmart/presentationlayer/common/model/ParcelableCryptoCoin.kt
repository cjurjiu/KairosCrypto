package com.catalinj.cryptosmart.presentationlayer.common.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a parcelable crypto coin
 * Created by catalin on 05/05/2018.
 */
data class ParcelableCryptoCoin constructor(val id: String,
                                            val name: String,
                                            val symbol: String,
                                            val rank: Int,
                                            val priceUsd: Float,
                                            val priceBtc: Float,
                                            val availableSupply: Long,
                                            val marketCapUsd: Double,
                                            val maxSupply: Long,
                                            val totalSupply: Long,
                                            val volumeUsd24h: Double,
                                            val percentChange24h: Float,
                                            val percentChange1h: Float,
                                            val percentChange7d: Float,
                                            val lastUpdated: Long) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readLong(),
            parcel.readDouble(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readDouble(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeInt(rank)
        parcel.writeFloat(priceUsd)
        parcel.writeFloat(priceBtc)
        parcel.writeLong(availableSupply)
        parcel.writeDouble(marketCapUsd)
        parcel.writeLong(maxSupply)
        parcel.writeLong(totalSupply)
        parcel.writeDouble(volumeUsd24h)
        parcel.writeFloat(percentChange24h)
        parcel.writeFloat(percentChange1h)
        parcel.writeFloat(percentChange7d)
        parcel.writeLong(lastUpdated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableCryptoCoin> {
        override fun createFromParcel(parcel: Parcel): ParcelableCryptoCoin {
            return ParcelableCryptoCoin(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableCryptoCoin?> {
            return arrayOfNulls(size)
        }
    }

}

