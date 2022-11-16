package com.ciejaycoding.letsmeat.repository.auth

import android.app.Activity
import com.ciejaycoding.letsmeat.models.Clients

import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider

interface AuthRepository {
    suspend fun verifyPhone(activity: Activity,phone : String,result : (UiState<String>) -> Unit)
    suspend fun verifyOTP(verificationCode: String, OTP: String,name : String,result: (UiState<String>) -> Unit)
    suspend fun getProfile(uid : String ,result: (UiState<Clients>) -> Unit)
}