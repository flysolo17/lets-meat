package com.ciejaycoding.letsmeat.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.auth.PhoneAuthProvider

data class AuthModel(
    val phone : String? = null,
    val forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null,
    val code : String? = null,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(PhoneAuthProvider.ForceResendingToken::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phone)
        parcel.writeParcelable(forceResendingToken, flags)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthModel> {
        override fun createFromParcel(parcel: Parcel): AuthModel {
            return AuthModel(parcel)
        }

        override fun newArray(size: Int): Array<AuthModel?> {
            return arrayOfNulls(size)
        }
    }

}