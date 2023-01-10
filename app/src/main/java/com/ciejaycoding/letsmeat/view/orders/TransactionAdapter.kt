package com.ciejaycoding.letsmeat.view.orders

import android.content.Context
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
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.OrderItems
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.computeItemPrice
import com.ciejaycoding.letsmeat.utils.countOrder
import com.ciejaycoding.letsmeat.utils.formatPrice
import com.ciejaycoding.letsmeat.utils.orderTotal

class TransactionAdapter(val context: Context, private val transactionsList: List<Transaction>, val fragmentPosition : Int) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){
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