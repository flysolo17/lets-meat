package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Address(
    val contacts: Contacts ? = null,
    val addressLine : String ? = null,
    val postalCode : Int ? = null ,
    val street : String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Contacts::class.java.classLoader),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(contacts, flags)
        parcel.writeString(addressLine)
        parcel.writeValue(postalCode)
        parcel.writeString(street)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }

}