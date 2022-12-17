package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable

class CartAndProduct(val products: Products ? = null, val cart: Cart? = null) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Products::class.java.classLoader),
        parcel.readParcelable(Cart::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(products, flags)
        parcel.writeParcelable(cart, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartAndProduct> {
        override fun createFromParcel(parcel: Parcel): CartAndProduct {
            return CartAndProduct(parcel)
        }

        override fun newArray(size: Int): Array<CartAndProduct?> {
            return arrayOfNulls(size)
        }
    }

}