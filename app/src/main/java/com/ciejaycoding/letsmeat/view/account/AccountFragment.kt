package com.ciejaycoding.letsmeat.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentAccountBinding
import com.ciejaycoding.letsmeat.databinding.FragmentCartBinding
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding : FragmentAccountBinding ? = null
    private val binding get() = _binding!!
    private val authViewModel : AuthViewModel by viewModels()

    private lateinit var loadingDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentAccountBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = ProgressDialog(view.context)
        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            detectUser(FirebaseAuth.getInstance().currentUser)
        }
        authViewModel.profile.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Failed -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    loadingDialog.showLoadingDialog("Fetching profile...")
                }
                is UiState.Success ->{
                    loadingDialog.stopLoading()
                    displayViews(it.data)
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
        user?.let {
            detectUser(user)
            authViewModel.getProfile(user.uid)

        }
    }
    private fun displayViews(client: Clients) {
        if (client.profile != null){
            Glide.with(binding.root.context).load(client.profile).into(binding.imageProfile)
        }
        binding.textFullname.text = client.fullname ?: "No name"
        binding.textPhoneNumber.text = client.phone
    }
    private fun detectUser(user : FirebaseUser?) {
        if (user == null) {
            binding.layoutNoUser.visibility = View.VISIBLE
            binding.layoutAccount.visibility = View.GONE
            return
        }
        binding.layoutNoUser.visibility = View.GONE
        binding.layoutAccount.visibility = View.VISIBLE
    }

}