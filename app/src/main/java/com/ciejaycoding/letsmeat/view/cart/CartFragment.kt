package com.ciejaycoding.letsmeat.view.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciejaycoding.letsmeat.MainActivity
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentCartBinding
import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.*
import com.ciejaycoding.letsmeat.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment(),CartAdapter.CartClickListener {
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel : CartViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private var mainActivity: MainActivity? = null
    private lateinit var cartAdapter : CartAdapter
    private lateinit var cartList : MutableList<Cart>
    private lateinit var cartAndProductList: MutableList<CartAndProduct>
    private  var user: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater,container,false)
        progressDialog = ProgressDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            cartViewModel.getAllCart(it.uid)
        }

        cartAndProductList= mutableListOf()
        cartList = mutableListOf()
        cartAdapter =CartAdapter(view.context,cartList,this@CartFragment)
        cartViewModel.cart.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Loading....")
                }
                is UiState.Success -> {
                    cartList.clear()
                    if (it.data.isEmpty()) {
                        binding.textCartIsEmpty.visibility = View.VISIBLE
                    } else {
                        binding.textCartIsEmpty.visibility = View.GONE
                    }
                    progressDialog.stopLoading()
                    it.data.map { cart ->
                        cartList.add(cart)
                    }
                    cartAdapter.notifyDataSetChanged()
                    displayCheckoutTotal(cartAndProductList)
                }
            }
        }

        detectUser(user)
        binding.buttonLogin.setOnClickListener { findNavController().navigate(R.id.action_navigation_cart_to_loginFragment2) }
        binding.recycleviewCart.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = cartAdapter
            addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))
        }
        binding.buttonCheckOut.setOnClickListener {
            val directions = CartFragmentDirections.actionNavigationCartToCheckOutFragment(cartAndProduct = cartAndProductList.toTypedArray())
            findNavController().navigate(directions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun detectUser(user : FirebaseUser?) {
        if (user != null) {
            binding.layoutNoUser.visibility = View.GONE
        } else {
            binding.layoutNoUser.visibility = View.VISIBLE
            binding.textCartIsEmpty.visibility = View.GONE
        }
    }

    override fun addQuantity(cartAndProduct: CartAndProduct,isChecked: Boolean) {
        user?.let {
            cartViewModel.increment(uid = it.uid, cartAndProduct = cartAndProduct).also {
                if(isChecked) {
                    displayCheckoutTotal(cartAndProductList)
                }
            }
        }

    }

    override fun decreaseQuantity(cartAndProduct: CartAndProduct,isChecked: Boolean) {
        user?.let {
            cartViewModel.decrement(uid = it.uid, cartAndProduct = cartAndProduct).also {
                if(isChecked) {
                    displayCheckoutTotal(cartAndProductList)
                }
            }
        }

    }

    override fun onCartClick(products: Products) {

    }

    override fun checkBoxIsClick(isChecked : Boolean,cartAndProduct: CartAndProduct) {
        if (isChecked) {
            cartAndProductList.add(cartAndProduct)
            displayCheckoutTotal(cartAndProductList)
        } else {
            cartAndProductList.filter { it.cart?.productID == cartAndProduct.cart?.productID }
                .forEach {
                    cartAndProductList.remove(it)
                }
            displayCheckoutTotal(cartAndProductList)
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity =  context as MainActivity
    }
    private fun displayCheckoutTotal(cartAndProductList : List<CartAndProduct>) {
        if (cartAndProductList.isEmpty()) {
            binding.checkOutLayout.visibility = View.GONE
            binding.textTotalCheckout.text = "0"
            return
        }
        binding.itemCount.text = "(${itemCount(cartAndProductList)})"
        binding.textTotalCheckout.text = formatPrice(computeProductTotal(cartAndProductList).toFloat())
        mainActivity?.hideNav().also {
            binding.checkOutLayout.visibility = View.VISIBLE
        }
    }

}