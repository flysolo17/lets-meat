package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

data class Order(
    var id : String? = null,
    val clientID : String? = null,
    val orderNumber : String ? =null,
    val address: Address ? = null,
    val items : List<OrderItems> ? = null,
    val message : String? = null,
    val date : Long ?= null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.createTypedArrayList(OrderItems),
        TODO("status"),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(clientID)
        parcel.writeString(orderNumber)
        parcel.writeParcelable(address, flags)
        parcel.writeTypedList(items)
        parcel.writeString(message)
        parcel.writeValue(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}