package com.ciejaycoding.letsmeat.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ciejaycoding.letsmeat.databinding.FragmentHomeBinding
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.view.home.indicators.IndicatorAdapter
import com.ciejaycoding.letsmeat.viewmodel.ProductsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(){
    private lateinit var binding : FragmentHomeBinding

    private val productsViewModel : ProductsViewModel by  viewModels()
    private lateinit var productsAdapter: ProductsAdapter


    private lateinit var indicatorAdapter: IndicatorAdapter
    private lateinit var productList : ArrayList<Products>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        activity?.actionBar?.hide()
        productList = arrayListOf()
        productsViewModel.getAllProducts()
        productsViewModel.products.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(context,state.message,Toast.LENGTH_LONG).show()
                }
                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Fetching Products....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()

                    productList.addAll(state.data)
                }
            }
            val category =  productList.map { it.category }.distinct()
            indicatorAdapter = IndicatorAdapter(this,category,productList)
            TabLayoutMediator(binding.tabLayout, binding.pager2.apply {
                adapter = indicatorAdapter
            }) { tab, position ->
                tab.text = category[position]
            }.attach()
        }


    }


}