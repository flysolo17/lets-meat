package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val id: String ? = "",
    val type: TransactionType ? = null,
    val clientID : String? = null,
    val order: Order? = null,
    val status: OrderStatus ? = null,
    val date: Long ? =0,
    val details: List<Details> = mutableListOf()) : Parcelable {

}


