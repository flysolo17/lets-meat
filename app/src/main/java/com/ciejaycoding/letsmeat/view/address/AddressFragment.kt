package com.ciejaycoding.letsmeat.view.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentAddressBinding
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.view.product.ViewProductArgs
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : Fragment(),AddressAdapter.AddressClickListener {

    private lateinit var binding : FragmentAddressBinding
    private val args : AddressFragmentArgs by navArgs()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var addressAdapter: AddressAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        addressAdapter = AddressAdapter(view.context,args.addresses.toList(), default = args.defaultAddress,this@AddressFragment)
        binding.recyclerViewAddress.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = addressAdapter
            addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))
        }

        binding.buttonAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment)
        }
        authViewModel.changeAddress.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Changing default Address....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onAddressClicked(position: Int) {
        if (args.defaultAddress != position) {
            displayChangeAddressDialog(position)
            return
        }

    }
    private fun displayChangeAddressDialog(position: Int) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Change Default Address")
            .setMessage("Are you sure you want to change your default address?")
            .setNegativeButton("Cancel") { dialog ,_ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") {dialog,_ ->
                authViewModel.changeDefaultAddress(args.uid,position).also {
                    addressAdapter.changeDefault(position)
                }
                dialog.dismiss()
            }
            .show()
    }


}