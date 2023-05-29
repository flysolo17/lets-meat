package com.ciejaycoding.letsmeat.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
        val type : PaymentType? = null,
        val total : Int ? = null,
        val details: PaymentDetails ? = null
) : Parcelable