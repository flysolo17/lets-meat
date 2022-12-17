package com.ciejaycoding.letsmeat.view.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentUpdateAccountBinding
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class UpdateAccount : Fragment() {
    private lateinit var binding : FragmentUpdateAccountBinding
    private val args : UpdateAccountArgs by navArgs()
    private var imageURI: Uri? = null
    private val authViewModel : AuthViewModel by viewModels()
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val data = result.data
            try {
                if (data?.data != null) {
                    imageURI = data.data
                    imageURI?.let {
                        Glide.with(binding.root.context).load(it).into(binding.imageClientProfile)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateAccountBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        args.client?.let {
            displayViews(it)
        }
        binding.buttonAddProfile.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }
        binding.buttonSave.setOnClickListener {
            if (imageURI != null) {
                FirebaseAuth.getInstance().currentUser?.let {
                    authViewModel.uploadImage(imageURI!!,it.uid)
                }
                return@setOnClickListener
            } else {
                args.client?.let { client->
                    client.fullname = binding.inputFullName.text.toString()
                    authViewModel.updateAccount(client)
                }

            }

        }
        authViewModel.uploadProfile.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Uploading profile...")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    args.client?.let { client->
                        client.fullname = binding.inputFullName.text.toString()
                        client.profile = it.data.toString()
                        authViewModel.updateAccount(client)
                    }
                }
            }
        }
        authViewModel.updateAccount.observe(viewLifecycleOwner){
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Updating profile...")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,"Updated successfully",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun displayViews(clients: Clients) {
        clients.profile?.let {
            Glide.with(binding.root.context).load(it).into(binding.imageClientProfile)
        }
        binding.inputFullName.setText(clients.fullname)
    }
}