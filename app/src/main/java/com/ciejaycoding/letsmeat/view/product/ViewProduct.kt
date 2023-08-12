package com.ciejaycoding.letsmeat.view.product

import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentViewProductBinding
import com.ciejaycoding.letsmeat.databinding.LoadingDialogBinding
import com.ciejaycoding.letsmeat.models.*
import com.ciejaycoding.letsmeat.utils.*
import com.ciejaycoding.letsmeat.view.product.adapter.CommentAdapter
import com.ciejaycoding.letsmeat.viewmodel.CartViewModel
import com.ciejaycoding.letsmeat.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewProduct : Fragment() {
    private val args: ViewProductArgs by navArgs()
    private val transactionViewModel : TransactionViewModel by viewModels()
    private var _binding : FragmentViewProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomDialog : BottomSheetDialog
    private val cartViewModel : CartViewModel by viewModels()
    private lateinit var loadingDialog: ProgressDialog
    private lateinit var transactionList : MutableList<Transaction>

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
        transactionList = mutableListOf()
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
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    loadingDialog.showLoadingDialog("Loading.....")
                }
                is UiState.Success ->{
                    loadingDialog.stopLoading()
                    transactionList.addAll(it.data)
                    binding.itemSold.text = getItemSoldTotal(productID = args.products.code!!,transactionList).toString()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun bindViews(products: Products) {
        transactionViewModel.getAllTransactions()
        if (products.images?.isNotEmpty()!!) {
            Glide.with(binding.root.context).load(products.images).into(binding.imageProduct)
        }
        if(products.comments!!.isNotEmpty()) {
            binding.ratingBar.rating = getCommentMedian(products.comments)
        }
        binding.textRatingTotal.text = getRatingSum(products.comments).toString()
        binding.textItemWeight.text ="${products.weight} ${products.weightType}"
        binding.textProductName.text = products.productName
        binding.textProductPrice.text = "₱ ${products.price}"
        binding.textProductDesc.text = products.description
        binding.textProductDetails.text = products.details
        if (products.comments.isEmpty()) {
            binding.textNoComment.visibility = View.VISIBLE
        } else {
            binding.textNoComment.visibility = View.GONE
            binding.recylerviewComments.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = CommentAdapter(binding.root.context,products.comments)
            }
            binding.textCommentCount.text = products.comments.size.toString()
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
        val textQuantity : EditText = view.findViewById(R.id.textQuantity)
        // Attach a TextWatcher to the EditText
        textQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is being changed.

                val newText = s.toString()
                if (newText.isNotEmpty()) {
                    try {
                        val intValue = Integer.parseInt(newText) // Convert the text to an integer
                        defaultQuantity =intValue
                        // Now you can use the intValue for further processing
                    } catch (e: NumberFormatException) {
                        // Handle the case where the input is not a valid integer
                    }
                } else {

                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    val quantity = s.toString().toInt()
                    if (quantity > args.products.quantity) {
                        textQuantity.setText("1")
                    }
                }

            }
        })
        textQuantity.setText(defaultQuantity.toString())
        view.findViewById<ImageButton>(R.id.buttonAdd).setOnClickListener {
            if (defaultQuantity < products.quantity) {
                defaultQuantity += 1
                textQuantity.setText(defaultQuantity.toString())
            }
        }
        view.findViewById<ImageButton>(R.id.buttonMinus).setOnClickListener {
            if (defaultQuantity >  1) {
                defaultQuantity -= 1
                textQuantity.setText(defaultQuantity.toString())
                return@setOnClickListener
            }
            Toast.makeText(view.context,"minimum purchase is 1!",Toast.LENGTH_SHORT).show()
        }
        buttonAddToCart.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.let {
                if (defaultQuantity > 0) {
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