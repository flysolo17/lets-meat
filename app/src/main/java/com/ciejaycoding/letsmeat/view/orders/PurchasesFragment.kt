package com.ciejaycoding.letsmeat.view.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ciejaycoding.letsmeat.databinding.FragmentPurchasesBinding
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.OrderStatus
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.PurchasesViewModel
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.collect.Iterables.addAll
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PurchasesFragment : Fragment() {
    private lateinit var binding : FragmentPurchasesBinding
    private val args : PurchasesFragmentArgs by navArgs()
    private val purchasesViewModel : PurchasesViewModel by viewModels()
    private val transactionViewModel : TransactionViewModel by viewModels()
    private var orderList : ArrayList<Order> = arrayListOf()
    private var transactionList : ArrayList<Transaction> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPurchasesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val progressDialog = ProgressDialog(view.context)
        currentUser?.let {
            purchasesViewModel.getAllPendingOrders(it.uid)
        }
        purchasesViewModel.orders.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Fetching orders....")
                }
                is UiState.Success -> {
                    orderList.clear()
                    progressDialog.stopLoading()
                    orderList.addAll(it.data)
                    transactionViewModel.getTransactions(currentUser!!.uid)
                }
            }
        }
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()

                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Getting all transactions....")
                }
                is UiState.Success -> {
                    transactionList.clear()
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,"Success!", Toast.LENGTH_SHORT).show()
                    transactionList.addAll(it.data)
                    attachTabs()
                }
            }
        }


    }
    private fun attachTabs() {
        if (orderList.isNotEmpty() || transactionList.isNotEmpty()) {
            val indicator = PurchasesTabAdapter(this,statusList(),orderList,transactionList)
            TabLayoutMediator(binding.tabLayout,binding.pager2.apply { adapter = indicator },true) {tab,position ->
                tab.text =statusList()[position].toString().replace("_"," ")
            }.attach()
            binding.tabLayout.getTabAt(args.tabPosition)!!.select()
        }
    }
    class PurchasesTabAdapter(val fragment: Fragment, private val statusList : Array<OrderStatus>, val orders : ArrayList<Order>,
                              private val transactions : ArrayList<Transaction>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return statusList.size
        }
        override fun createFragment(position: Int): Fragment {
            val fragment = OrderStatusFragment()


            fragment.arguments = Bundle().apply {
                putInt(POSITION,position)
                putParcelableArrayList("orders",orders)
                putParcelableArrayList("transactions",transactions)
            }
            return fragment
        }
    }
    companion object {
        const val POSITION = "position"
    }

    fun statusList() : Array<OrderStatus> {
        var arr = arrayOf<OrderStatus>()
        arr = OrderStatus.values().filter { it != OrderStatus.TO_PACKED }.toTypedArray()
        return arr
    }

    override fun onResume() {
        super.onResume()
        orderList.clear()
        transactionList.clear()

    }
}