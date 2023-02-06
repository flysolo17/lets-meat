package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderItems(
    val id : String  ? = null,
    val image: String ? = null,
    val name : String  ? = null,
    val quantity : Int  ? = null,
    val originalPrice : Float ? = null,
    val cost : Float ? = null,
    val weight : Float ? = null
) : Parcelable{


}