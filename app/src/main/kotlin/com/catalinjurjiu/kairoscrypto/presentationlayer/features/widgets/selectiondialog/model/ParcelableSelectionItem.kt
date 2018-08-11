package com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.selectiondialog.model

import android.os.Parcel
import android.os.Parcelable
import com.catalinjurjiu.common.ParcelableNamedComponent
import com.catalinjurjiu.common.extensions.toBoolean
import com.catalinjurjiu.common.extensions.toInt

/**
 * Created by catalin on 24/04/2018.
 */
data class ParcelableSelectionItem(override val name: String,
                                   val value: String,
                                   val activeItem: Boolean = false) : ParcelableNamedComponent {
    constructor(parcel: Parcel) : this(
            name = parcel.readString(),
            value = parcel.readString(),
            activeItem = parcel.readInt().toBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value)
        parcel.writeInt(activeItem.toInt())
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
    return ParcelableSelectionItem(this.name, this.value, this.isActive)
}

fun ParcelableSelectionItem.toSelectionItem(): SelectionItem {
    return SelectionItem(this.name, this.value, this.activeItem)
}
