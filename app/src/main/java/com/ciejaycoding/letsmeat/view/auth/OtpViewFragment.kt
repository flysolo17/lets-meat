package com.ciejaycoding.letsmeat.view.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentOtpViewBinding
import com.ciejaycoding.letsmeat.models.AuthModel
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpViewFragment : Fragment() {
    private val args : OtpViewFragmentArgs by navArgs()
    private val authViewModel : AuthViewModel by viewModels()
    private var authModel : AuthModel  ?= null
    private lateinit var binding : FragmentOtpViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authModel = args.authModel
    }
    companion object
    {
        const val TAG = "OTP"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpViewBinding.inflate(inflater,container,false)
        authModel?.let {
            verificationCodeCountDown()
            binding.textPhoneNumber.text = "+63 ${it.phone}"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireActivity())

        binding.buttonConfirm.setOnClickListener {
            val input = binding.inputPinView.text.toString()
            if (input.isNotEmpty() && input.length == 6) {
                authModel?.let {
                    authViewModel.verifyOtp(it.code!!, input)
                }
                return@setOnClickListener
            }
            Toast.makeText(view.context,"Invalid code",Toast.LENGTH_SHORT).show()
        }

        authViewModel.verifyOTP.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("verifying otp....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    authViewModel.checkUser(it.data)
                }
            }
        }
        authViewModel.checkUser.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Checking user...")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    if (it.data !== null) {
                        authViewModel.createUser(it.data)
                    } else {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }

                }
            }
        }

        authViewModel.createUser.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Creating user....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }
        }
        binding.buttonResendOTP.setOnClickListener {
            authModel?.let {
                authViewModel.resendOTP(requireActivity(),it.phone!!,it.forceResendingToken!!)
            }

        }
        authViewModel.resendOTP.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    authModel = it.data
                    verificationCodeCountDown()
                }
            }
        }
    }
    private fun verificationCodeCountDown() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.buttonResendOTP.text = "" + millisUntilFinished / 1000
                binding.buttonResendOTP.isEnabled = false
            }
            override fun onFinish() {
                binding.buttonResendOTP.text = "Resend"
                binding.buttonResendOTP.isEnabled = true
            }
        }.start()
    }
}