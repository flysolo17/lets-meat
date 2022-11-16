package com.ciejaycoding.letsmeat.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.models.Products

class ProductsAdapter(private val context : Context, private val productList : List<Products>,val productAdapterClickListener: ProductAdapterClickListener) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    interface ProductAdapterClickListener {
        fun onProductionClick(products: Products)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_products,parent,false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val products = productList[position]
        holder.textProductName.text = products.productName
        holder.textPrice.text = "₱ ${products.price}"
        if (products.images!!.isNotEmpty()) {
            holder.imageProduct.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(context).load(products.images).into(holder.imageProduct)
        }
        holder.itemView.setOnClickListener {
            productAdapterClickListener.onProductionClick(products)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
    class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textProductName : TextView = itemView.findViewById(R.id.textSample)
        val textPrice : TextView = itemView.findViewById(R.id.textPrice)
        val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
        val textProductSold : TextView = itemView.findViewById(R.id.itemSold)
    }
}