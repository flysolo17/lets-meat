package com.ciejaycoding.letsmeat.view.home.indicators

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentMenuProductsBinding
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.view.home.HomeFragmentDirections
import com.ciejaycoding.letsmeat.view.home.ProductsAdapter


class MenuProductsFragment : Fragment() ,ProductsAdapter.ProductAdapterClickListener{

    private lateinit var binding : FragmentMenuProductsBinding
    private lateinit var productsAdapter: ProductsAdapter

    private var products : List<Products> ? =null
    private var position : Int? = null
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
        // Inflate the layout for this fragment
        binding = FragmentMenuProductsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewProducts.apply {
            layoutManager = GridLayoutManager(view.context,2)
            adapter = ProductsAdapter(view.context,products!!,this@MenuProductsFragment)
        }
    }

    override fun onProductionClick(products: Products) {
        val action = HomeFragmentDirections.actionNavigationHomeToViewProduct(products)
        findNavController().navigate(action)
    }
}