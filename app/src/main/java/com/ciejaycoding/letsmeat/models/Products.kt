package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Products(
    val code: String? = "",
    val userID: String? = "",
    val images: String? = "",
    val productName: String? = "",
    val cost: Float= 0f,
    val price: Float = 0f,
    val quantity: Int= 0,
    val description: String? = "",
    val details: String? = "",
    val isAvailable: Boolean= false,
    val comments: List<Comments> ? = listOf(),
    val createdAt: Long = 0 ,
    val expiration : Long = 0,
    val category : String = "",
    val weight : Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(Comments),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(userID)
        parcel.writeString(images)
        parcel.writeString(productName)
        parcel.writeDouble(cost.toDouble())
        parcel.writeDouble(price.toDouble())
        parcel.writeInt(quantity)
        parcel.writeString(description)
        parcel.writeString(details)
        parcel.writeByte(if (isAvailable) 1 else 0)
        parcel.writeTypedList(comments)
        parcel.writeLong(createdAt)
        parcel.writeLong(expiration)
        parcel.writeString(category)
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
