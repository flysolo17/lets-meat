package com.ciejaycoding.letsmeat.view.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentOrderStatusBinding
import com.ciejaycoding.letsmeat.models.*
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.PurchasesViewModel
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderStatusFragment : Fragment(),PurchasesAdapter.OrderClickListener {
    private val transactionViewModel : TransactionViewModel by viewModels()
    private val purchasesViewModel : PurchasesViewModel by viewModels()
    private lateinit var binding : FragmentOrderStatusBinding
    private var orders: List<Order> ? = null
    private var transactions: List<Transaction> ? = null
    private var position: Int ? = null
    private lateinit var progressDialog: ProgressDialog
    private var uid : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orders =it.getParcelableArrayList("orders")
            transactions  =it.getParcelableArrayList("transactions")
            position = it.getInt(PurchasesFragment.POSITION)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderStatusBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = FirebaseAuth.getInstance().currentUser?.uid
        progressDialog = ProgressDialog(view.context)
        if (position == 0) {
            binding.recyclerviewTransactions.visibility = View.GONE
            binding.recyclerviewOrders.visibility = View.VISIBLE
            binding.recyclerviewOrders.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = PurchasesAdapter(view.context,orders!!,position!!,this@OrderStatusFragment)
            }
        } else {
            binding.recyclerviewTransactions.visibility = View.VISIBLE
            binding.recyclerviewOrders.visibility = View.GONE
            var list = mutableListOf<Transaction>()
            when (position) {
                1 -> {
                    list = (transactions?.filter { it.status == OrderStatus.TO_PACKED } as MutableList<Transaction>?)!!
                }
                2 -> {
                    list = (transactions?.filter { it.status == OrderStatus.TO_SHIP } as MutableList<Transaction>?)!!
                }
                3 -> {
                    list = (transactions?.filter { it.status == OrderStatus.TO_RECEIVE } as MutableList<Transaction>?)!!
                }
                4 -> {
                    list = (transactions?.filter { it.status == OrderStatus.COMPLETED } as MutableList<Transaction>?)!!
                }
                5 -> {
                    list = (transactions?.filter { it.status == OrderStatus.DECLINED } as MutableList<Transaction>?)!!
                }
                6 -> {
                    list = (transactions?.filter { it.status == OrderStatus.CANCELED } as MutableList<Transaction>?)!!
                }
            }
            binding.recyclerviewTransactions.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = TransactionAdapter(view.context,list,position!!)
            }
        }
        transactionViewModel.cancelTransaction.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading ->{
                    progressDialog.showLoadingDialog("Cancelling order.....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    findNavController().popBackStack()
                    purchasesViewModel.deleteOrder(it.data)
                }
            }
        }
        purchasesViewModel.deleteOrder.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,it.message,
                        Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading ->{
                    progressDialog.showLoadingDialog("Deleting order.....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun cancelOrder(position: Int) {
        val order = orders?.get(position)
        if (order!= null) {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes") { _, _ ->
                    cancel(order)
                }.setNegativeButton("No") { dialog ,_ ->
                    dialog.dismiss()
                }
                .show()
        }

    }
    private fun cancel(order: Order) {
        val details = Details(
            OrderStatus.CANCELED,
            "Order Cancelled",
            "Cancelled by client",
            System.currentTimeMillis()
        )
        val transaction = Transaction(
            order.orderNumber,
            TransactionType.ONLINE,
            uid,
            order,
            OrderStatus.CANCELED,
            System.currentTimeMillis(),
            listOf(details),
        )
        transactionViewModel.cancelTransaction(transaction)
    }



}