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
import com.ciejaycoding.letsmeat.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() , ProductsAdapter.ProductAdapterClickListener{
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val productsViewModel : ProductsViewModel by  viewModels()
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context)
        activity?.actionBar?.hide()

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
                    productsAdapter = ProductsAdapter(view.context,state.data,this)
                    binding.recyclerviewProducts.apply {
                        layoutManager = GridLayoutManager(context,2)
                        adapter = productsAdapter
                    }
                    progressDialog.stopLoading()
                }
            }

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onProductionClick(products: Products) {
        val action = HomeFragmentDirections.actionNavigationHomeToViewProduct(products)
        Navigation.findNavController(binding.root).navigate(action)

    }

}