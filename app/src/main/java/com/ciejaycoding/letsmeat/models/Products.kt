package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Products(
    val code: String? = "",
    val userID: String? = "",
    val images: String? = "",
    val productName: String? = "",
    val cost: Float= 0f,
    val price: Float = 0f,
    val quantity: Int= 0,
    val description: String? = "",
    val details: String? = "",
    val isAvailable: Boolean= false,
    val comments: List<Comments> ? = listOf(),
    val createdAt: Long = 0 ,
    val expiration : Long = 0,
    val category : String = "",
    val weight : Float = 0f
) : Parcelable
