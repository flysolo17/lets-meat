package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Cart(
    val productID : String ? = null,
    var quantity : Int = 0,
    val addedAt : Long ? = null,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),

        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productID)
        parcel.writeInt(quantity)
        parcel.writeValue(addedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }

}