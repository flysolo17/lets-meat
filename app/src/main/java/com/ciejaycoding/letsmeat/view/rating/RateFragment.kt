package com.ciejaycoding.letsmeat.view.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentRateBinding
import com.ciejaycoding.letsmeat.models.Comments
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.view.rating.adapter.RatingAdapter
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFragment : Fragment() ,RatingAdapter.RatingClickListener{

    private lateinit var binding : FragmentRateBinding
    private val transactionViewModel : TransactionViewModel by viewModels()
    private var user: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        FirebaseAuth.getInstance().currentUser?.let {
            user = it
            transactionViewModel.getToRateList(it.uid)
        }
        transactionViewModel.rateList.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Getting Transactions today!")
                }
                is UiState.Success -> {
                    Toast.makeText(view.context,"Success",Toast.LENGTH_SHORT).show()
                    progressDialog.stopLoading()
                    binding.recyclerviewRate.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = RatingAdapter(view.context,it.data,this@RateFragment)
                    }
                }
            }
        }
        transactionViewModel.rate.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Saving Comment")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun rateThisTransaction(transaction: Transaction) {
        val new = arrayListOf<String>()
        transaction.order?.items?.map {
            if(it.id != null ) {
                new.add(it.id)
            }
        }
        showRatingBottomDialog(new)
    }
    private fun showRatingBottomDialog(orderList : List<String>) {
        val bottomDialog = BottomSheetDialog(binding.root.context, R.style.BottomsheetDialogStyle)
        val view : View = LayoutInflater.from(binding.root.context).inflate(R.layout.rating_bottom_dialog,binding.root,false)
        view.findViewById<Button>(R.id.buttonSubmitRating).setOnClickListener {
            val rating = view.findViewById<RatingBar>(R.id.ratingBar)
            val comment = view.findViewById<TextInputEditText>(R.id.inputComment)
            user?.let {
                val comments = Comments(it.uid,comment.text.toString(),rating.rating,System.currentTimeMillis())
                transactionViewModel.addComment(orderList,comments)
            }
        }
        bottomDialog.setContentView(view)
        bottomDialog.show()
    }
}