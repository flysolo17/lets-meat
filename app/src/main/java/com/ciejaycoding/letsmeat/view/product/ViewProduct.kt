package com.ciejaycoding.letsmeat.view.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentViewProductBinding
import com.ciejaycoding.letsmeat.models.Products
import com.google.android.material.bottomsheet.BottomSheetDialog


class ViewProduct : Fragment() {
    private val args: ViewProductArgs by navArgs()

    private var _binding : FragmentViewProductBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(args.products)
        binding.buttonAddToCart.setOnClickListener {
            showBottomDialog(args.products)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun bindViews(products: Products) {
        if (products.images?.isNotEmpty()!!) {
            Glide.with(binding.root.context).load(products.images).into(binding.imageProduct)
        }
        binding.textProductName.text = products.productName
        binding.textProductPrice.text = "₱ ${products.price}"
        binding.textProductDesc.text = products.description
        binding.textProductDetails.text = products.details
        if (products.comments?.size == 0) {
            binding.textNoComment.visibility = View.VISIBLE
        } else {
            binding.textNoComment.visibility = View.GONE
        }
    }

    private fun showBottomDialog(products: Products) {
        val bottomDialog = BottomSheetDialog(requireActivity(), R.style.BottomsheetDialogStyle)
        val view : View = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_add_cart,binding.root,false)
        if (products.images!!.isNotEmpty()) {
            Glide.with(binding.root.context).load(products.images)
                .into(view.findViewById(R.id.imageProduct))
        }
        view.findViewById<TextView>(R.id.textProductPrice).text = "₱ ${products.price}"
        view.findViewById<TextView>(R.id.textStocks).text = products.quantity.toString()
        bottomDialog.setContentView(view)
        bottomDialog.show()
    }


}