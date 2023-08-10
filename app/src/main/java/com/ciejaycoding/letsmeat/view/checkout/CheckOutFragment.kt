package com.ciejaycoding.letsmeat.view.checkout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.IOException

@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private lateinit var binding : FragmentCheckOutBinding
    private val args : CheckOutFragmentArgs by navArgs()
    private var user : FirebaseUser ? = null
    private val authViewModel : AuthViewModel by viewModels()
    private val cartViewModel : CartViewModel by viewModels()
    private var clients : Clients ? = null
    private var paymentType = PaymentType.CASH_ON_DELIVERY
    private lateinit var progressDialog : ProgressDialog
    private var imageURI: Uri? = null
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val data = result.data
            try {
                if (data?.data != null) {
                    imageURI = data.data
                    binding.textReceipt.text = imageURI?.lastPathSegment ?: "No name"
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOutBinding.inflate(inflater,container,false)
        args.cartAndProduct.map {
            iterateCart(it)
        }
        val cartAndProduct = args.cartAndProduct.toList()
        displayInfo(cartAndProduct)
        progressDialog = ProgressDialog(binding.root.context)
        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            authViewModel.getProfile(it.uid)
        }
        return binding.root
    }
    private fun  displayInfo(cartAndProduct: List<CartAndProduct>) {
        val type =  if (binding.textTransactionType.text.toString() == "DELIVER") 0 else 1

        binding.textGcashName.text = "Gcash name: Juan Dela Cruz"
        binding.textGcashNumber.text = "Gcash #: 09322384675"
        binding.textItemSubtotal.text = formatPrice(computeProductTotal(cartAndProduct).toFloat())
        binding.textTotalCount.text = "(${itemCount(cartAndProduct)} items):"
        binding.textTotalWithoutTax.text =formatPrice(computeTotalWithOutTax(computeProductTotal(cartAndProduct)).toFloat())
        binding.textTax.text = formatPrice(computeTotalTax(computeProductTotal(cartAndProduct)).toFloat())
        binding.textTotalWeight.text= "${formatWeight(countTotalWeight(cartAndProduct).toFloat())}kg"
        binding.texShippingFee.text = formatPrice(computeShipping(countTotalWeight(cartAndProduct),type).toFloat())
        binding.textTotal.text = formatPrice(computeShipping(countTotalWeight(cartAndProduct),type).toFloat() + computeProductTotal(cartAndProduct).toFloat())
        binding.textTotalPayment.text = formatPrice(computeShipping(countTotalWeight(cartAndProduct),type).toFloat() + computeProductTotal(cartAndProduct).toFloat())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        binding.textTransactionType.setOnClickListener {
            val items = arrayOf("DELIVER","PICK_UP")
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Select")
                .setItems(items) { dialog, which ->
                    val cartAndProduct = args.cartAndProduct.toList()
                    binding.textTransactionType.text = items[which]
                    displayInfo(cartAndProduct)

                }
                .show()
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radioGcash) {
                binding.layoutGcash.visibility =View.VISIBLE
                paymentType = PaymentType.GCASH
            } else {
                binding.layoutGcash.visibility = View.GONE
                paymentType = PaymentType.CASH_ON_DELIVERY
            }
        }



        binding.buttonAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_checkOutFragment_to_addAddressFragment)
        }
        binding.buttonAddReceipt.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }
        binding.layoutAddressInfo.setOnClickListener {
            clients?.let {
                val directions =
                    CheckOutFragmentDirections.actionCheckOutFragmentToAddressFragment(
                            it.addresses!!.toTypedArray(),
                            it.id!!,
                            it.defaultAddress
                    )
                    findNavController().navigate(directions)
                }
            }
        binding.buttonCheckOutOrder.setOnClickListener {
            val message = binding.inputMessage.text.toString()
            if (clients!!.addresses?.get(clients!!.defaultAddress) == null) {
                Toast.makeText(view.context, "Please select address!", Toast.LENGTH_SHORT)
                        .show()
                return@setOnClickListener
            }
            clients?.let { client ->
                val items = args.cartAndProduct.map { item ->
                    OrderItems(
                        item.products?.code!!,
                        item.products.images!!,
                        item.products.productName!!,
                        item.cart!!.quantity,
                        item.products.price,
                        item.products.cost,
                        item.products.weight,
                        item.products.weightType
                    )
                }.toList()

                val order = Order(
                    "", client.id,
                    orderType = if (binding.textTransactionType.text.equals("DELIVER")) OrderType.DELIVER else OrderType.PICK_UP,
                    generateOrderNumber(),
                    address = client.addresses?.get(client.defaultAddress),
                    items = items,
                    message = message,
                    date = System.currentTimeMillis())
                    showCheckoutDialog(order)
                }
        }
    }

    private fun showCheckoutDialog(order: Order) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Terms And Conditions")
            .setMessage("By clicking okay you agree to our terms and conditions.")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Okay") { dialog, _ ->
                if (paymentType == PaymentType.GCASH) {
                    getGcashInfo(user?.uid!!,order)
                } else {
                    val type =  if (binding.textTransactionType.equals("DELIVER")) 0 else 1
                    val total = computeShipping(countTotalWeight(args.cartAndProduct.toList()),type) + computeProductTotal(args.cartAndProduct.toList()).toFloat()
                    order.payment = Payment(PaymentType.CASH_ON_DELIVERY,
                        total.toInt(),PaymentDetails("","",""))
                    cartViewModel.checkoutOrder(order = order).also {
                        args.cartAndProduct.map { cart ->
                            cart.cart?.let {
                                cartViewModel.removeFromCart(
                                    order.clientID!!,
                                    it.productID!!
                                )
                            }
                        }
                        dialog.dismiss()
                    }
                }
            }
            .show()
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

    private fun getGcashInfo(uid : String,order: Order){
        val phone = binding.edtPhone.text.toString()
        val amount = binding.edtAmount.text.toString()
        val referene = binding.edtReference.text.toString()
        if (phone.isEmpty()) {
            binding.edtPhone.error ="This field is required"
        } else if (referene.isEmpty()) {
            binding.edtReference.error = "this field is required"
        }  else if (amount.isEmpty()) {
            binding.edtReference.error = "this field is required"
        } else if (imageURI == null) {
            Toast.makeText(binding.root.context,"Please add gcash receipt",Toast.LENGTH_SHORT).show()
        } else {
            order.payment = Payment(PaymentType.GCASH,amount.toInt(),PaymentDetails(phone,referene,""))
            cartViewModel.uploadReceipt(order, imageURI!!,uid)
        }

    }
    private fun observers() {
        authViewModel.profile.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Checking out order....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }
        }
        cartViewModel.uploadReceipt.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failed -> {
                    progressDialog.stopLoading()
                    Toast.makeText(binding.root.context, state.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    progressDialog.showLoadingDialog("Uploading profile...")
                }

                is UiState.Success -> {
                    progressDialog.stopLoading()
                    cartViewModel.checkoutOrder(order = state.data).also {
                        args.cartAndProduct.map { cart ->
                            cart.cart?.let {
                                cartViewModel.removeFromCart(state.data.clientID!!, it.productID!!)
                            }
                        }
                    }
                }
            }
        }
    }
}