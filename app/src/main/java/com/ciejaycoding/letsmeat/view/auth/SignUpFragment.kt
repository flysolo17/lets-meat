package com.ciejaycoding.letsmeat.view.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciejaycoding.letsmeat.databinding.FragmentSignUpBinding
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SignUpFragment : Fragment() {


    private var _binding : FragmentSignUpBinding ? = null
    private  val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }
    private lateinit var loadingDialog: ProgressDialog
    private  var code : String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = ProgressDialog(view.context)

        binding.buttonSend.setOnClickListener {
            val phone  = binding.inputPhone.text.toString()
            if (phone.startsWith("9") && phone.length == 10) {
                authViewModel.verifyPhoneNumber(requireActivity(),phone)
                return@setOnClickListener
            }
            Toast.makeText(view.context,"Invalid",Toast.LENGTH_SHORT).show()
        }
        binding.buttonCreateAccount.setOnClickListener {
            val sms = binding.inputVerificationCode.text.toString()
            val name = binding.inputName.text.toString()
            if (code == null || sms.isEmpty() || name.isEmpty()) {
                Toast.makeText(view.context,"Please fill up all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.verifyOtp(code!!,sms,name)
        }
        authViewModel.verify.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    verificationCodeCountDown()
                }
                is UiState.Success -> {
                    code = it.data
                }
            }
        }
        authViewModel.verifyOTP.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                    loadingDialog.stopLoading()
                }
                UiState.Loading -> {
                    loadingDialog.showLoadingDialog("Verifying OTP")
                }
                is UiState.Success -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun verificationCodeCountDown() {
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.buttonSend.text = "" + millisUntilFinished / 1000
                binding.buttonSend.isEnabled = false
            }

            override fun onFinish() {
                binding.buttonSend.text = "Resend"
                binding.buttonSend.isEnabled = true
            }
        }.start()
    }
}