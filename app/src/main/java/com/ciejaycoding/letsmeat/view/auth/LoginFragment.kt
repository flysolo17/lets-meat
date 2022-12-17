package com.ciejaycoding.letsmeat.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentLoginBinding
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
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
            val phone  = binding.inputPhone.text.toString()
            if (phone.startsWith("9") && phone.length == 10) {
                val directions = LoginFragmentDirections.actionLoginFragmentToOtpViewFragment(phone)
                findNavController().navigate(directions)
                return@setOnClickListener
            }
            binding.inputPhoneLayout.error = "Invalid Phone number"
            Toast.makeText(view.context,"Invalid", Toast.LENGTH_SHORT).show()
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