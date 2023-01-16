package com.ciejaycoding.letsmeat.view.orders

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentViewTransactionBinding
import com.ciejaycoding.letsmeat.models.Details
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.*
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewTransactionFragment : Fragment() {
    private val transactionViewModel : TransactionViewModel by viewModels()
    private val args : ViewTransactionFragmentArgs by navArgs()
    private lateinit var binding : FragmentViewTransactionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewTransactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        transactionViewModel.getTransactionByID(args.transactionID)
        transactionViewModel.getTransactionByID.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Getting Transaction...")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    displayData(it.data)
                }
            }
        }
    }

    private fun displayData(data: Transaction) {
        binding.textOrderNumber.text = data.id
        binding.textAddress.text = data.order?.address?.addressLine + "\n" + data.order?.address?.street + " | " + data.order?.address?.postalCode
        binding.textPerson.text = data.order?.address?.contacts?.name + " | " + (data.order?.address?.contacts?.phone
            ?: "")
        binding.textItems.text = countOrder(data.order!!)
        binding.textTotal.text = formatPrice(orderTotal(data.order).toFloat())
        data.details.reversed().mapIndexed { index, details ->
            displayDetails(details,index)
        }
    }
    private fun displayDetails(detail : Details,index : Int) {
        val view : View = LayoutInflater.from(binding.root.context).inflate(R.layout.order_status,null,false)
        view.findViewById<TextView>(R.id.textDate).text = dateFormat(detail.date!!)
        view.findViewById<TextView>(R.id.textTime).text = timeFormat(detail.date)
        val status = view.findViewById<TextView>(R.id.textStatus)
        val message = view.findViewById<TextView>(R.id.textMessage)
        if (index == 0) {
            status.setTextColor(Color.BLUE)
            message.setTextColor(Color.BLUE)
        }
        status.text = detail.status.toString()
        message.text = detail.message
        binding.layoutStatus.addView(view)
    }

}