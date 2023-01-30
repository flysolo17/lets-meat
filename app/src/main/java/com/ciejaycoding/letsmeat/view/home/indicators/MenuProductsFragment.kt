package com.ciejaycoding.letsmeat.view.home.indicators

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ciejaycoding.letsmeat.databinding.FragmentMenuProductsBinding
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.TRANSACTION_TABLE
import com.ciejaycoding.letsmeat.utils.getItemSoldTotal
import com.ciejaycoding.letsmeat.view.home.HomeFragmentDirections
import com.ciejaycoding.letsmeat.view.home.ProductsAdapter
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MenuProductsFragment : Fragment() ,ProductsAdapter.ProductAdapterClickListener{
    private lateinit var binding : FragmentMenuProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var firestore : FirebaseFirestore
    private var products : List<Products> ? =null
    private var position : Int? = null
    private val transactionViewModel : TransactionViewModel by  viewModels()
    private lateinit var transactionList : MutableList<Transaction>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            products = it.getParcelableArrayList(IndicatorAdapter.ARG_PRODUCTS)
            position = it.getInt(IndicatorAdapter.ARG_POSITION)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuProductsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        transactionList = mutableListOf()
        getTransactions()
        if (position == 0) {
            binding.inputSearch.visibility = View.VISIBLE
        } else {
            binding.inputSearch.visibility = View.GONE
        }
        productsAdapter = ProductsAdapter(binding.root.context,products!!,transactionList,this@MenuProductsFragment)
        binding.recyclerviewProducts.apply {
            layoutManager = GridLayoutManager(binding.root.context,2)
            adapter = productsAdapter
        }
        binding.inputSearch.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        productsAdapter = ProductsAdapter(binding.root.context,products!!.filter { it.productName!!.lowercase(
                            Locale.getDefault()
                        )
                            .contains(p0.toString().lowercase(Locale.getDefault()))  },transactionList,this@MenuProductsFragment)
                        binding.recyclerviewProducts.apply {
                            layoutManager = GridLayoutManager(binding.root.context,2)
                            adapter = productsAdapter
                        }
                    } else {
                        productsAdapter = ProductsAdapter(binding.root.context,products!!,transactionList,this@MenuProductsFragment)
                        binding.recyclerviewProducts.apply {
                            layoutManager = GridLayoutManager(binding.root.context,2)
                            adapter = productsAdapter
                        }
                    }
                }
            }

        })

    }

    override fun onProductionClick(products: Products) {
        val action = HomeFragmentDirections.actionNavigationHomeToViewProduct(products)
        findNavController().navigate(action)
    }
    private fun getTransactions() {
        firestore.collection(TRANSACTION_TABLE)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    transactionList.clear()
                    for(snapshot in it.result) {
                        val transaction  = snapshot.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                    productsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(binding.root.context,"Failed getting transactions",Toast.LENGTH_SHORT).show()
                }
            }
    }
}