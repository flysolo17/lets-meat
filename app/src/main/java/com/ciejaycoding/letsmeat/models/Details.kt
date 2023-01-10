package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Details(
    val status:  OrderStatus ? = null,
    val message: String ? = "",
    val updatedBy: String ? = "",
    val date: Long ? = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("status"),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(updatedBy)
        parcel.writeValue(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Details> {
        override fun createFromParcel(parcel: Parcel): Details {
            return Details(parcel)
        }

        override fun newArray(size: Int): Array<Details?> {
            return arrayOfNulls(size)
        }
    }
}