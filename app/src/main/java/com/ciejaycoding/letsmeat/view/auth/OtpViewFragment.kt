package com.ciejaycoding.letsmeat.view.auth

import android.os.Bundle
import android.os.CountDownTimer
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
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpViewFragment : Fragment() {
    private val args : OtpViewFragmentArgs by navArgs()
    private val authViewModel : AuthViewModel by viewModels()
    private var resendingToken : PhoneAuthProvider.ForceResendingToken ? = null
    private lateinit var authModel : AuthModel
    private lateinit var binding : FragmentOtpViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireActivity())
        binding.textPhoneNumber.text = "+63 ${args.phone}"
        authViewModel.sendOtp(requireActivity(),args.phone)

        binding.buttonConfirm.setOnClickListener {
            val input = binding.inputPinView.text.toString()
            if (input.isNotEmpty() && input.length == 6) {
                authViewModel.verifyOtp(authModel.code!!, input)
                return@setOnClickListener
            }
            Toast.makeText(view.context,"Invalid code",Toast.LENGTH_SHORT).show()
        }
        //observers
        authViewModel.sendOTP.observe(viewLifecycleOwner) {
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
        authViewModel.verifyOTP.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
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
                    it.data?.let {client ->
                        authViewModel.createUser(client)
                        return@observe
                    }
                    findNavController().popBackStack()
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
                }
            }
        }
        binding.buttonResendOTP.setOnClickListener {

                authViewModel.resendOTP(requireActivity(),authModel.phone!!,authModel.forceResendingToken!!)

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