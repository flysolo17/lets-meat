package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

class OrderItems(
    val id : String  ? = null,
    val image: String ? = null,
    val name : String  ? = null,
    val quantity : Int  ? = null,
    val originalPrice : Float ? = null,
    val cost : Float ? = null,
    val weight : Int ? = null
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeValue(quantity)
        parcel.writeValue(originalPrice)
        parcel.writeValue(cost)
        parcel.writeValue(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItems> {
        override fun createFromParcel(parcel: Parcel): OrderItems {
            return OrderItems(parcel)
        }

        override fun newArray(size: Int): Array<OrderItems?> {
            return arrayOfNulls(size)
        }
    }

}