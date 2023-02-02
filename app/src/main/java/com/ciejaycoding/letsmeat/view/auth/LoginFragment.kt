package com.ciejaycoding.letsmeat.view.auth

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
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
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding ? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        val directions = LoginFragmentDirections.actionLoginFragmentToOtpViewFragment(phone)
                        findNavController().navigate(directions)
                        return@setPositiveButton
                    }
                    binding.inputPhoneLayout.error = "Invalid Phone number"
                    Toast.makeText(view.context,"Invalid", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .show()
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