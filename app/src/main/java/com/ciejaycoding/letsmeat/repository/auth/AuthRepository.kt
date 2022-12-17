package com.ciejaycoding.letsmeat.repository.auth

import android.app.Activity
import android.net.Uri
import com.ciejaycoding.letsmeat.models.Address
import com.ciejaycoding.letsmeat.models.AuthModel
import com.ciejaycoding.letsmeat.models.Clients

import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider

interface AuthRepository {
    suspend fun verifyPhone(activity: Activity,phone : String,result : (UiState<AuthModel>) -> Unit)
    suspend fun resendOTP(activity: Activity,phone: String,resendingToken: PhoneAuthProvider.ForceResendingToken ,result: (UiState<AuthModel>) -> Unit)
    suspend fun verifyOTP(verificationCode: String, OTP: String,result: (UiState<Clients>) -> Unit)
    suspend fun getProfile(uid : String ,result: (UiState<Clients>) -> Unit)
    suspend fun createAccount(clients: Clients,result: (UiState<String>) -> Unit)
    suspend fun checkIfExists(clients: Clients,result: (UiState<Clients?>) -> Unit)
    suspend fun addAddress(uid : String,address: Address,result: (UiState<String>) -> Unit)
    suspend fun changeDefaultAddress(uid: String,position : Int,result: (UiState<String>) -> Unit)
    suspend fun updateAccount(clients: Clients,result: (UiState<String>) -> Unit)
    suspend fun uploadProfile(imageUri: Uri,uid: String,result: (UiState<Uri>) -> Unit)
}