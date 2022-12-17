package com.ciejaycoding.letsmeat.repository.auth

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.ciejaycoding.letsmeat.models.Address
import com.ciejaycoding.letsmeat.models.AuthModel

import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.CLIENTS_TABLE
import com.ciejaycoding.letsmeat.utils.PROFILE_STORAGE
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.android.gms.tasks.Task
import com.google.common.io.Files.getFileExtension
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
class AuthRepositoryImpl(private val auth : FirebaseAuth,private val firestore: FirebaseFirestore,val firebaseStorage: FirebaseStorage,val context: Context) : AuthRepository {

    /**
     * This method allows the user to send verification code.
     * @param phone This parameter is needed to send verification code.
     */
    override suspend fun verifyPhone(activity: Activity,phone: String, result: (UiState<AuthModel>) -> Unit) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+63$phone") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    Toast.makeText(activity,"Completed!",Toast.LENGTH_SHORT).show()
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failed(e.message!!))
                    } else if (e is FirebaseTooManyRequestsException) {
                        result.invoke(UiState.Failed(e.message!!))
                    }
                }
                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    result.invoke(UiState.Success(AuthModel(phone, forceResendingToken,s)))
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun resendOTP(
        activity: Activity,
        phone: String,
        resendingToken:  ForceResendingToken,
        result: (UiState<AuthModel>) -> Unit
    ) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+63$phone")       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity)                 // Activity (for callback binding)
                .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        Toast.makeText(activity,"Completed!",Toast.LENGTH_SHORT).show()
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        if (e is FirebaseAuthInvalidCredentialsException) {
                            result.invoke(UiState.Failed(e.message!!))
                        } else if (e is FirebaseTooManyRequestsException) {
                            result.invoke(UiState.Failed(e.message!!))
                        }
                    }
                    override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                        result.invoke(UiState.Success(AuthModel(phone, forceResendingToken, code = s)))
                    }
                })
                .setForceResendingToken(resendingToken)// OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * This method verifies the OTP that the user inputs.
     * @param verificationCode This parameter is autogenerated by firebase phone auth you will receive this code via text message
     * @param OTP This parameter is comes from user input. This parameter should match otherwise the user phone number shouldn't verified
     */
    override suspend fun verifyOTP(
        verificationCode: String,
        OTP: String,
        result: (UiState<Clients>) -> Unit
    ) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, OTP)
        result.invoke(UiState.Loading)
        auth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = auth.currentUser!!
                    val client = Clients(id= user.uid,"",user.phoneNumber,"No Name")
                    result.invoke(UiState.Success(client))
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failed("Invalid Credential!"))
                    } else {
                        result.invoke(UiState.Failed("Auth Failed! "))
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failed("Authentication Failed:  ${it.message}"))
            }
    }

    override suspend fun getProfile(uid: String, result: (UiState<Clients>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE).document(uid)
            .get()
            .addOnSuccessListener {snapshot ->
                if (snapshot.exists()) {
                    val clients = snapshot.toObject(Clients::class.java)
                    clients?.let {
                        result.invoke(UiState.Success(it))
                    }
                } else {
                    result.invoke(UiState.Failed("User does not exists!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failed(it.message!!))
            }
    }

    override suspend fun addAddress(uid: String,address: Address ,result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE)
            .document(uid)
            .update("addresses",FieldValue.arrayUnion(address))
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result.invoke(UiState.Success("New Address Added!"))
                } else {
                    result.invoke(UiState.Failed("Adding Address Failed!"))
                }
            }
    }
    override suspend fun createAccount(clients: Clients,result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE).document(clients.id!!)
            .set(clients)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully created!"))
                } else {
                    result.invoke(UiState.Failed("Failed creating account"))
                }
            }
    }

    override suspend fun checkIfExists( clients: Clients,result: (UiState<Clients?>) -> Unit) {
        firestore.collection(CLIENTS_TABLE).document(clients.id!!)
            .get()
            .addOnSuccessListener { task ->
                if (task.exists()) {
                    result.invoke(UiState.Success(null))
                    return@addOnSuccessListener
                }
                result.invoke(UiState.Success(clients))
            }.addOnFailureListener {
                result.invoke(UiState.Failed("Failed Checking user!"))
            }
    }




    override suspend fun changeDefaultAddress(
        uid: String,
        position: Int,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE)
            .document(uid)
            .update("defaultAddress",position)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Default address Change successfully"))
                } else {
                    result.invoke(UiState.Failed("error"))
                }
            }
    }

    override suspend fun updateAccount(clients: Clients, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE)
            .document(clients.id!!)
            .set(clients)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Updated!"))
                } else {
                    result.invoke(UiState.Failed("Update account failed!"))
                }
            }
    }

    override suspend fun uploadProfile(imageUri: Uri, uid: String,result: (UiState<Uri>) -> Unit) {
        val storage  = firebaseStorage.getReference(uid).child(PROFILE_STORAGE).child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri.toString()))
        result.invoke(UiState.Loading)
        try {
            val uri: Uri = withContext(Dispatchers.IO) {
                    storage
                    .putFile(imageUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            result.invoke(UiState.Success(uri))
        } catch (e: FirebaseException){
            result.invoke(UiState.Failed(e.message!!))
        }catch (e: Exception){
            result.invoke(UiState.Failed(e.message!!))
        }
    }

}