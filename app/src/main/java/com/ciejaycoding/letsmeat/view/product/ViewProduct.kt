package com.ciejaycoding.letsmeat.view.product

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentViewProductBinding
import com.ciejaycoding.letsmeat.databinding.LoadingDialogBinding
import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewProduct : Fragment() {
    private val args: ViewProductArgs by navArgs()

    private var _binding : FragmentViewProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomDialog : BottomSheetDialog
    private val cartViewModel : CartViewModel by viewModels()
    private lateinit var loadingDialog: ProgressDialog
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
        loadingDialog = ProgressDialog(requireActivity())
        bottomDialog = BottomSheetDialog(requireActivity(), R.style.BottomsheetDialogStyle)
        bindViews(args.products)
        binding.buttonAddToCart.setOnClickListener {
            showBottomDialog(args.products,0)
        }
        binding.buttonBuyNow.setOnClickListener {
            showBottomDialog(args.products,1)

        }
        cartViewModel.addToCart.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    loadingDialog.showLoadingDialog("Adding to cart...")
                }
                is UiState.Success -> {
                    loadingDialog.stopLoading()
                    bottomDialog.dismiss()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                }

            }
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
        binding.textItemWeight.text ="${products.weight}kg"
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

    private fun showBottomDialog(products: Products,type : Int) {
        var defaultQuantity = 1
        val view : View = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_add_cart,binding.root,false)
        if (products.images!!.isNotEmpty()) {
            Glide.with(binding.root.context).load(products.images)
                .into(view.findViewById(R.id.imageProduct))
        }
        val buttonAddToCart = view.findViewById<MaterialButton>(R.id.buttonAddToCart)
        val buttonBuyNow = view.findViewById<MaterialButton>(R.id.buttonBuyNow)
        if (type == 0) {
            buttonAddToCart.visibility = View.VISIBLE
            buttonBuyNow.visibility = View.GONE
        } else {
            buttonAddToCart.visibility = View.GONE
            buttonBuyNow.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.textProductPrice).text = "₱ ${products.price}"
        view.findViewById<TextView>(R.id.textStocks).text = products.quantity.toString()
        val textQuantity : TextView = view.findViewById(R.id.textQuantity)
        textQuantity.text = defaultQuantity.toString()
        view.findViewById<ImageButton>(R.id.buttonAdd).setOnClickListener {
            if (defaultQuantity < products.quantity) {
                defaultQuantity += 1
                textQuantity.text = defaultQuantity.toString()
            }
        }
        view.findViewById<ImageButton>(R.id.buttonMinus).setOnClickListener {
            if (defaultQuantity >  1) {
                defaultQuantity -= 1
                textQuantity.text = defaultQuantity.toString()
                return@setOnClickListener
            }
            Toast.makeText(view.context,"minimum purchase is 1!",Toast.LENGTH_SHORT).show()
        }
        buttonAddToCart.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.let {
                if (defaultQuantity != 0) {
                    val  cart = Cart(products.code,defaultQuantity,System.currentTimeMillis())
                    cartViewModel.addToCart(uid = it.uid,cart)
                }
                return@setOnClickListener
            }
            showLoginDialog()
        }
        buttonBuyNow.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.let {
                if (defaultQuantity != 0) {
                    val cartAndProduct = CartAndProduct(products,
                        Cart(args.products.code,defaultQuantity,System.currentTimeMillis())
                    )
                    val directions = ViewProductDirections.actionViewProductToCheckOutFragment(arrayOf(cartAndProduct))
                    findNavController().navigate(directions).also {
                        bottomDialog.dismiss()
                    }
                }
                return@setOnClickListener
            }
            showLoginDialog()
        }
        bottomDialog.setContentView(view)
        bottomDialog.show()
    }

    private fun showLoginDialog() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("No user!")
            .setMessage("Ooops it looks like you haven't login yet!")
            .setPositiveButton("Login") {dialog, _ ->
                bottomDialog.dismiss()
                dialog.dismiss()
                findNavController().navigate(R.id.action_viewProduct_to_loginFragment)
            }.show()
    }


}