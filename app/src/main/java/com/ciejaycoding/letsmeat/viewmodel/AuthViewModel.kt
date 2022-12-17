package com.ciejaycoding.letsmeat.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Address
import com.ciejaycoding.letsmeat.models.AuthModel
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.repository.auth.AuthRepository
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository)  : ViewModel(){
    private val _sendOTP= MutableLiveData<UiState<AuthModel>>()
    val sendOTP : LiveData<UiState<AuthModel>> get() = _sendOTP


    private val _verificationOTP = MutableLiveData<UiState<Clients>>()
    val verifyOTP : LiveData<UiState<Clients>> get() = _verificationOTP


    private val _getProfile = MutableLiveData<UiState<Clients>>()
    val profile : LiveData<UiState<Clients>> get() = _getProfile

    private val _addAddress = MutableLiveData<UiState<String>>()
    val addAddress : LiveData<UiState<String>> get() = _addAddress


    private val _changeAddress= MutableLiveData<UiState<String>>()
    val changeAddress : LiveData<UiState<String>> get() = _changeAddress

    private val _resendOTP = MutableLiveData<UiState<AuthModel>>()
    val resendOTP : LiveData<UiState<AuthModel>> get() = _resendOTP


    private val _checkUser= MutableLiveData<UiState<Clients?>>()
    val checkUser : LiveData<UiState<Clients?>> get() = _checkUser
    private val _createUser = MutableLiveData<UiState<String>>()
    val createUser : LiveData<UiState<String>> get() = _createUser
    private val _updateAccount = MutableLiveData<UiState<String>>()
    val updateAccount : LiveData<UiState<String>> get() = _updateAccount

    private val _uploadProfile = MutableLiveData<UiState<Uri>>()
    val uploadProfile : LiveData<UiState<Uri>> get() = _uploadProfile
    fun sendOtp(activity: Activity,phone : String) {
        viewModelScope.launch {
            authRepository.verifyPhone(activity,phone) {
                _sendOTP.value = it
            }
        }
    }

    fun verifyOtp(verificationCode: String,OTP: String) {
        viewModelScope.launch {
            authRepository.verifyOTP(verificationCode,OTP) {
                _verificationOTP.value = it
            }
        }
    }
    fun getProfile(uid : String) {
        viewModelScope.launch {
            authRepository.getProfile(uid) {
                _getProfile.postValue(it)
            }
        }
    }
    fun addAddress(uid: String,address: Address) {
        viewModelScope.launch {
            authRepository.addAddress(uid,address) {
                _addAddress.value =it
            }
        }
    }
    fun changeDefaultAddress(uid: String,position : Int) {
        viewModelScope.launch {
            authRepository.changeDefaultAddress(uid,position) {
                _changeAddress.value = it
            }
        }
    }

    fun resendOTP(activity: Activity,phone: String,resendingToken: PhoneAuthProvider.ForceResendingToken) {
        viewModelScope.launch {
            authRepository.resendOTP(activity = activity, phone = phone,resendingToken = resendingToken) {
                _resendOTP.value = it
            }
        }
    }
    fun checkUser(clients: Clients) {
        viewModelScope.launch {
            authRepository.checkIfExists(clients) {
                _checkUser.value = it
            }
        }
    }
    fun createUser(clients: Clients) {
        viewModelScope.launch {
            authRepository.createAccount(clients) {
                _createUser.value = it
            }
        }
    }
    fun updateAccount(clients: Clients) {
        viewModelScope.launch {
            authRepository.updateAccount(clients) {
                _updateAccount.value = it
            }
        }
    }
    fun uploadImage(imageURI : Uri,uid : String) {
        viewModelScope.launch {
            authRepository.uploadProfile(imageURI,uid) {
                _uploadProfile.value = it
            }
        }
    }
}