package com.ciejaycoding.letsmeat.view.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.PRODUCTS_TABLE
import com.ciejaycoding.letsmeat.utils.formatPrice
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(val context: Context, private val cartList: List<Cart>, private val cartClickListener: CartClickListener) :
    RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder>()  {
    interface CartClickListener {
        fun addQuantity(cartAndProduct: CartAndProduct,isChecked: Boolean)
        fun decreaseQuantity(cartAndProduct: CartAndProduct,isChecked: Boolean)
        fun onCartClick(products: Products)
        fun checkBoxIsClick(isChecked :Boolean,cartAndProduct: CartAndProduct)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapterViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_cart,parent,false)
        return CartAdapterViewHolder(view)
    }
    override fun onBindViewHolder(holder: CartAdapterViewHolder, position: Int) {
        val model = cartList[position]
        model.productID?.let { holder.getProduct(it) }
        holder.quantity.text = model.quantity.toString()
        holder.buttonAdd.setOnClickListener {
            if (model.quantity < holder.product?.quantity!!) {
                model.quantity += 1
                cartClickListener.addQuantity(CartAndProduct(cart = model, products = holder.product),holder.checkBox.isChecked).also {
                    holder.quantity.text = model.quantity.toString()
                }
                return@setOnClickListener
            } else {
                Toast.makeText(context,"Not enough Stocks!",Toast.LENGTH_SHORT).show()
            }
        }
        holder.buttonMinus.setOnClickListener {
            if (model.quantity > 1) {
                model.quantity -= 1
                cartClickListener.decreaseQuantity(
                    CartAndProduct(
                        cart = model,
                        products = holder.product
                    ),
                    holder.checkBox.isChecked
                ).also {

                    holder.quantity.text = model.quantity.toString()
                }
            } else {
                Toast.makeText(context,"Oops minimum purchase is 1!",Toast.LENGTH_SHORT).show()
            }
        }
        holder.itemView.setOnClickListener {
            cartClickListener.onCartClick(holder.product!!)
        }
        holder.checkBox.setOnClickListener {
            cartClickListener.checkBoxIsClick(holder.checkBox.isChecked, CartAndProduct(
                cart = model,
                products = holder.product
            ))
        }
    }
    override fun getItemCount(): Int {
        return cartList.size
    }

    class CartAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
        private val productName : TextView = itemView.findViewById(R.id.textProductName)
        private val productPrice : TextView = itemView.findViewById(R.id.textPrice)
        private val productStocks : TextView = itemView.findViewById(R.id.textStocks)
        val quantity : TextView = itemView.findViewById(R.id.textQuantity)
        val buttonMinus : ImageButton = itemView.findViewById(R.id.buttonMinus)
        val buttonAdd : ImageButton = itemView.findViewById(R.id.buttonAdd)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        var product : Products? = null
        private val firestore = FirebaseFirestore.getInstance()
        fun setCheckBox(isChecked: Boolean) {
            checkBox.isChecked = isChecked
        }
        fun getProduct(productID : String) {
            firestore.collection(PRODUCTS_TABLE).document(productID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        product = task.result.toObject(Products::class.java)
                        if (product != null) {
                            product!!.images?.let {
                                Glide.with(itemView.context).load(it).into(imageProduct)
                            }
                            productStocks.text = product!!.quantity.toString() + " stocks left"
                            productName.text = product!!.productName.toString()
                            productPrice.text = formatPrice(product!!.price)
                        }
                    }
                }
        }
    }



}