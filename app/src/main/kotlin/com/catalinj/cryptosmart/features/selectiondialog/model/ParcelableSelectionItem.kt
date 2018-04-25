package com.catalinj.cryptosmart.features.selectiondialog.model

import android.os.Parcel
import android.os.Parcelable
import com.catalinjurjiu.common.ParcelableNamedComponent

/**
 * Created by catalin on 24/04/2018.
 */
data class ParcelableSelectionItem(override val name: String,
                                   val value: String) : ParcelableNamedComponent {
    constructor(parcel: Parcel) :
            this(name = parcel.readString(), value = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableSelectionItem> {
        override fun createFromParcel(parcel: Parcel): ParcelableSelectionItem {
            return ParcelableSelectionItem(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableSelectionItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun SelectionItem.toParcelableSelectionItem(): ParcelableSelectionItem {
    return ParcelableSelectionItem(this.name, this.value)
}

fun ParcelableSelectionItem.toSelectionItem(): SelectionItem {
    return SelectionItem(this.name, this.value)
}
