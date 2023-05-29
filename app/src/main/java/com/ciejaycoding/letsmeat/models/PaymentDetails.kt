package com.ciejaycoding.letsmeat.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetails(
    val phone : String? = null,
    val referenceNumber : String? = null,
    var image : String? = null,
) : Parcelable
