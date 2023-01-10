package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable


data class Transaction(
    val id: String ? = "",
    val type: TransactionType ? = null,
    val clientID : String? = null,
    val order: Order? = null,
    val status: OrderStatus ? = null,
    val date: Long ? =0,
    val details: List<Details> = mutableListOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        TODO("type"),
        parcel.readString(),
        parcel.readParcelable(Order::class.java.classLoader),
        TODO("status"),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        TODO("details")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(clientID)
        parcel.writeParcelable(order, flags)
        parcel.writeValue(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}


