package com.ciejaycoding.letsmeat.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.repository.auth.AuthRepository
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository)  : ViewModel(){
    private val _verification = MutableLiveData<UiState<String>>()
    val verify : LiveData<UiState<String>> get() = _verification


    private val _verificationOTP = MutableLiveData<UiState<String>>()
    val verifyOTP : LiveData<UiState<String>> get() = _verificationOTP


    private val _getProfile = MutableLiveData<UiState<Clients>>()
    val profile : LiveData<UiState<Clients>> get() = _getProfile

    fun verifyPhoneNumber(activity: Activity,phone : String) {
        viewModelScope.launch {
            authRepository.verifyPhone(activity,phone) {
                _verification.value = it
            }
        }
    }

    fun verifyOtp(verificationCode: String,OTP: String,name : String) {
        viewModelScope.launch {
            authRepository.verifyOTP(verificationCode,OTP,name) {
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

}