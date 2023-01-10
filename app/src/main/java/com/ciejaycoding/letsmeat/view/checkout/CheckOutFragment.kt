package com.ciejaycoding.letsmeat.view.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentCheckOutBinding
import com.ciejaycoding.letsmeat.models.*
import com.ciejaycoding.letsmeat.utils.*
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.ciejaycoding.letsmeat.viewmodel.CartViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private lateinit var binding : FragmentCheckOutBinding
    private val args : CheckOutFragmentArgs by navArgs()
    private var user : FirebaseUser ? = null
    private val authViewModel : AuthViewModel by viewModels()
    private val cartViewModel : CartViewModel by viewModels()
    private var clients : Clients ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.cartAndProduct.map {
            iterateCart(it)
        }
        val cartAndProduct = args.cartAndProduct.toList()
        binding.textItemSubtotal.text = formatPrice(computeProductTotal(cartAndProduct).toFloat())
        binding.textTotalCount.text = "(${itemCount(cartAndProduct)} items):"
        binding.textSubtotal.text =formatPrice(computeShippingFee(cartAndProduct).toFloat())
        binding.textTotalWeight.text= countTotalWeight(cartAndProduct).toString()
        binding.texShippingFee.text = computeShippingFee(cartAndProduct).toString()
        binding.textTotal.text = formatPrice(computeShippingFee(cartAndProduct) + computeProductTotal(cartAndProduct).toFloat())
        binding.textTotalPayment.text = formatPrice(computeShippingFee(cartAndProduct) + computeProductTotal(cartAndProduct).toFloat())
        val progressDialog = ProgressDialog(view.context)
        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            authViewModel.getProfile(it.uid)
        }
        authViewModel.profile.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Getting Address")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    clients = it.data
                    clients?.let {client->
                        displayAddressInfo(client)
                    }

                }
            }
        }
        cartViewModel.checkoutOrder.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Checking out order....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
        binding.buttonAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_checkOutFragment_to_addAddressFragment)
        }
        binding.layoutAddressInfo.setOnClickListener {
            clients?.let {
                val directions = CheckOutFragmentDirections.actionCheckOutFragmentToAddressFragment(it.addresses!!.toTypedArray(),it.id!!,it.defaultAddress)
                findNavController().navigate(directions)
            }
        }
        binding.buttonCheckOutOrder.setOnClickListener {
            val message = binding.inputMessage.text.toString()
            if (clients!!.addresses?.get(clients!!.defaultAddress) == null) {
                Toast.makeText(view.context,"Please select address!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            clients?.let { client ->
                val items = args.cartAndProduct.map { item -> OrderItems(
                    item.products?.code!!,
                    item.products.images!!,
                    item.products.productName!!,
                    item.cart!!.quantity,
                    item.products.price,
                    item.products.cost,
                    item.products.weight)
                }.toList()

                val order = Order(
                    "",
                    client.id,
                    generateOrderNumber(),
                    address = client.addresses?.get(client.defaultAddress),
                    items = items,
                    message = message,
                    date = System.currentTimeMillis()
                )
                cartViewModel.checkoutOrder(order = order).also {
                    cartAndProduct.map {  cart->
                        cart.cart?.let {
                            cartViewModel.removeFromCart(order.clientID!!,it.productID!!)
                        }
                    }

                }

            }

        }

    }

    private fun displayAddressInfo(clients: Clients) {
        if (!clients.addresses.isNullOrEmpty()) {
            binding.layoutAddressInfo.visibility = View.VISIBLE
            binding.textContact.text = "${clients.addresses[clients.defaultAddress].contacts?.name} | ${clients.addresses[clients.defaultAddress].contacts?.phone}"
            val add = clients.addresses[clients.defaultAddress].addressLine?.split("\n")
            binding.textAddressLine.text = add?.joinToString(", ")
            binding.textStreet.text = clients.addresses[clients.defaultAddress].street
        } else {
            binding.layoutAddressInfo.visibility = View.GONE
        }

    }
    private fun iterateCart(cartAndProduct: CartAndProduct) {
        val view : View = LayoutInflater.from(binding.root.context).inflate(R.layout.layout_checkout,binding.layoutItems,false)
        view.findViewById<TextView>(R.id.itemName).text = cartAndProduct.products?.productName
        view.findViewById<TextView>(R.id.itemPrice).text = formatPrice(computePrice(cartAndProduct))
        view.findViewById<TextView>(R.id.itemQuantity).text = "${cartAndProduct.cart?.quantity}x"
        val productImage : ImageView =  view.findViewById(R.id.itemImage)
        if (!cartAndProduct.products!!.images.isNullOrEmpty()) {
            productImage.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(binding.root.context).load(cartAndProduct.products.images).into(productImage)
        }
        binding.layoutItems.addView(view)
    }



}