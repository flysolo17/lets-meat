package com.ciejaycoding.letsmeat.view.rating.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.models.OrderItems
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.computeItemPrice
import com.ciejaycoding.letsmeat.utils.countOrder
import com.ciejaycoding.letsmeat.utils.formatPrice
import com.ciejaycoding.letsmeat.utils.orderTotal
import com.google.android.material.divider.MaterialDivider

class RatingAdapter(private val context : Context ,val transactionList : List<Transaction>,val ratingClickListener: RatingClickListener) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {
    interface RatingClickListener {
        fun rateThisTransaction(transaction: Transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_rate,parent,false)
        return RatingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val  order = transactionList[position].order
        transactionList[position].order?.items?.map {
            holder.displayItems(it)
        }
        holder.textStatus.text = transactionList[position].status.toString()
        holder.textOrderTotal.text = formatPrice(orderTotal(order!!).toFloat())
        holder.textItemTotal.text = countOrder(order)

        holder.divider.visibility = View.GONE
        holder.textStatus.setTextColor(Color.RED)
        holder.buttonRate.setOnClickListener {
            ratingClickListener.rateThisTransaction(transactionList[position])
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
    class RatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        val layoutItems: LinearLayout = itemView.findViewById(R.id.layoutItems)
        val textItemTotal: TextView = itemView.findViewById(R.id.itemTotal)
        val textOrderTotal: TextView = itemView.findViewById(R.id.textOrderTotal)
        val buttonRate: Button = itemView.findViewById(R.id.buttonToRate)
        val divider : MaterialDivider = itemView.findViewById(R.id.divider)
        fun displayItems(items: OrderItems) {
            val view: View = LayoutInflater.from(itemView.context)
                .inflate(R.layout.layout_checkout, layoutItems, false)
            view.findViewById<TextView>(R.id.itemName).text = items.name
            view.findViewById<TextView>(R.id.itemPrice).text = formatPrice(computeItemPrice(items))
            view.findViewById<TextView>(R.id.itemQuantity).text = "${items.quantity}x"
            val productImage: ImageView = view.findViewById(R.id.itemImage)
            if (!items.image.isNullOrEmpty()) {
                productImage.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(itemView.context).load(items.image).into(productImage)
            }
            layoutItems.addView(view)
        }
    }
}