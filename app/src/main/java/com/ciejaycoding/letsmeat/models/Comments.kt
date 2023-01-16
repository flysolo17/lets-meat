package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

class Comments(
    val clientID :String? = "",
    val comment: String ? = "",
    val rating: Float ? = 0f,
    val date : Long ? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(clientID)
        parcel.writeString(comment)
        parcel.writeValue(rating)
        parcel.writeValue(date)
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<Comments> {
        override fun createFromParcel(parcel: Parcel): Comments {
            return Comments(parcel)
        }

        override fun newArray(size: Int): Array<Comments?> {
            return arrayOfNulls(size)
        }
    }
}

