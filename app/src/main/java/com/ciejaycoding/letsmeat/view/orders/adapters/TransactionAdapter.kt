package com.ciejaycoding.letsmeat.view.orders.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

class TransactionAdapter(val context: Context, private val transactionsList: List<Transaction>, val fragmentPosition : Int,val transactionClickListener: TransactionClickListener) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){
    interface TransactionClickListener {
        fun viewTransaction(id : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_order,parent,false)
        return TransactionViewHolder(view)
    }
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val  order = transactionsList[position].order
        transactionsList[position].order?.items?.map {
            holder.displayItems(it)
        }
        holder.textStatus.text = transactionsList[position].status.toString()
        holder.textOrderTotal.text = formatPrice(orderTotal(order!!).toFloat())
        holder.textItemTotal.text = countOrder(order)
        holder.buttonCancel.visibility = View.GONE
        holder.divider.visibility = View.GONE
        holder.itemView.setOnClickListener {
            transactionClickListener.viewTransaction(transactionsList[position].id!!)
        }
        holder.textStatus.setTextColor(Color.RED)
    }

    override fun getItemCount(): Int {
        return transactionsList.size
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        val layoutItems: LinearLayout = itemView.findViewById(R.id.layoutItems)
        val buttonCancel = itemView.findViewById<Button>(R.id.buttonCancel)
        val textItemTotal: TextView = itemView.findViewById(R.id.itemTotal)
        val textOrderTotal: TextView = itemView.findViewById(R.id.textOrderTotal)
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