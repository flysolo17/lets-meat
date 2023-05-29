package com.ciejaycoding.letsmeat.view.auth

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentLoginBinding
import com.ciejaycoding.letsmeat.models.AuthModel
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.view.auth.OtpViewFragment.Companion.TAG
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding ? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var progressDialog : ProgressDialog
    private val auth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(view.context)
        binding.buttonContinue.setOnClickListener {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("Terms And Conditions")
                .setMessage("By clicking okay you agree to our terms and conditions.")
                .setNegativeButton("Cancel") { dialog,_ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Okay") { dialog,_ ->
                    val phone  = binding.inputPhone.text.toString()
                    if (phone.startsWith("9") && phone.length == 10) {
                        authViewModel.sendOtp(requireActivity(),phone);
                        return@setPositiveButton
                    }
                    binding.inputPhoneLayout.error = "Invalid Phone number"
                    Toast.makeText(view.context,"Invalid", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .show()
        }
        //observers
        authViewModel.sendOTP.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    val directions = LoginFragmentDirections.actionLoginFragmentToOtpViewFragment(it.data)
                    findNavController().navigate(directions)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findNavController().popBackStack()
        }
    }
}