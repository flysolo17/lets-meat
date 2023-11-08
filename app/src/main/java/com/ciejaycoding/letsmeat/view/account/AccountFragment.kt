package com.ciejaycoding.letsmeat.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentAccountBinding

import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding : FragmentAccountBinding ? = null
    private val binding get() = _binding!!
    private val authViewModel : AuthViewModel by viewModels()

    private lateinit var loadingDialog: ProgressDialog
    private lateinit var client: Clients
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
        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_account_to_loginFragment)
        }
        binding.buttonPending.setOnClickListener {
            val directions = AccountFragmentDirections.actionNavigationAccountToPurchasesFragment(0)
            findNavController().navigate(directions)
        }
        binding.buttonToShip.setOnClickListener {
            val directions = AccountFragmentDirections.actionNavigationAccountToPurchasesFragment(1)
            findNavController().navigate(directions)
        }
        binding.buttonToPickUp.setOnClickListener {
            val directions = AccountFragmentDirections.actionNavigationAccountToPurchasesFragment(2)
            findNavController().navigate(directions)
        }
        binding.buttonToRecieve.setOnClickListener {
            val directions = AccountFragmentDirections.actionNavigationAccountToPurchasesFragment(3)
            findNavController().navigate(directions)
        }
        binding.buttonToRate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_account_to_rateFragment)
        }
        binding.buttonLogout.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Log out")
                .setMessage("Are you sure you want to logged out? ")
                .setPositiveButton("Yes") {dialog,_->
                    FirebaseAuth.getInstance().signOut()
                    detectUser(FirebaseAuth.getInstance().currentUser)
                }
                .setNegativeButton("Cancel") {dialog,_->
                    dialog.dismiss()
                }
                .show()

        }
        binding.buttonHistory.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_account_to_historyFragment)
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
                    client = it.data
                    loadingDialog.stopLoading()
                    displayViews(it.data)
                }
            }
        }
        binding.buttonMyAddresses.setOnClickListener {
            val action = AccountFragmentDirections.actionNavigationAccountToAddressFragment(
                addresses = client.addresses?.toTypedArray() ?: emptyArray(),
                 client.id!!,
                defaultAddress = client.defaultAddress
            )
            findNavController().navigate(action)
        }
        binding.buttonEditProfile.setOnClickListener {
            val  action = AccountFragmentDirections.actionNavigationAccountToUpdateAccount(client)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        detectUser(user)
        user?.let {
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