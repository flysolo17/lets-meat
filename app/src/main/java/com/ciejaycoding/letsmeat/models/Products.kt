package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Products(
    val code: String? = "",
    val userID: String? = "",
    val images: String? = "",
    val productName: String? = "",
    val cost: Int= 0,
    val price: Int= 0,
    val quantity: Int= 0,
    val description: String? = "",
    val details: String? = "",
    val isAvailable: Boolean= false,
    val comments: List<Comments> ? = listOf(),
    val createdAt: Long = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(Comments),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(userID)
        parcel.writeString(images)
        parcel.writeString(productName)
        parcel.writeInt(cost)
        parcel.writeInt(price)
        parcel.writeInt(quantity)
        parcel.writeString(description)
        parcel.writeString(details)
        parcel.writeByte(if (isAvailable) 1 else 0)
        parcel.writeTypedList(comments)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Products> {
        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }

        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }
    }

}

