package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var id : String? = null,
    val clientID : String? = null,
    val orderType : OrderType ? =null,
    val orderNumber : String ? =null,
    val address: Address ? = null,
    val items : List<OrderItems> ? = null,
    val message : String?= null,
    var payment: Payment ? = null,
    val date : Long ?= null,
) : Parcelable


enum class OrderType {
    DELIVER  , PICK_UP ,WALK_IN
}