package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Clients(
    val id : String ? = null,
    var profile : String? = null,
    val phone : String ? = null,
    var fullname : String ? = null,
    val addresses : List<Address> ? = null,
    val defaultAddress : Int  = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Address),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(profile)
        parcel.writeString(phone)
        parcel.writeString(fullname)
        parcel.writeTypedList(addresses)
        parcel.writeInt(defaultAddress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Clients> {
        override fun createFromParcel(parcel: Parcel): Clients {
            return Clients(parcel)
        }

        override fun newArray(size: Int): Array<Clients?> {
            return arrayOfNulls(size)
        }
    }
}
