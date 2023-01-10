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
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.OrderItems
import com.ciejaycoding.letsmeat.models.OrderStatus
import com.ciejaycoding.letsmeat.utils.*

class PurchasesAdapter(val context: Context,val orderList: List<Order>,val fragmentPosition : Int,val orderClickListener: OrderClickListener)  : RecyclerView.Adapter<PurchasesAdapter.PurchasesViewHolder>(){

    interface OrderClickListener {
        fun cancelOrder(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasesViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_order,parent,false)
        return PurchasesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchasesViewHolder, position: Int) {
        orderList[position].items?.map {
            holder.displayItems(it)
        }
        holder.textOrderTotal.text = formatPrice(orderTotal(orderList[position]).toFloat())
        holder.textItemTotal.text = countOrder(orderList[position])
        if(fragmentPosition == 0) {
            holder.buttonCancel.visibility = View.VISIBLE
        } else {
            holder.buttonCancel.visibility = View.GONE
        }
        holder.buttonCancel.setOnClickListener {
            orderClickListener.cancelOrder(position)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class PurchasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        val layoutItems: LinearLayout = itemView.findViewById(R.id.layoutItems)
        val buttonCancel = itemView.findViewById<Button>(R.id.buttonCancel)
        val textItemTotal : TextView = itemView.findViewById(R.id.itemTotal)
        val textOrderTotal : TextView = itemView.findViewById(R.id.textOrderTotal)

        fun displayItems(items : OrderItems) {
            val view : View = LayoutInflater.from(itemView.context).inflate(R.layout.layout_checkout,layoutItems,false)
            view.findViewById<TextView>(R.id.itemName).text = items.name
            view.findViewById<TextView>(R.id.itemPrice).text = formatPrice(computeItemPrice(items))
            view.findViewById<TextView>(R.id.itemQuantity).text = "${items.quantity}x"
            val productImage : ImageView =  view.findViewById(R.id.itemImage)
            if (!items.image.isNullOrEmpty()) {
                productImage.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(itemView.context).load(items.image).into(productImage)
            }
            layoutItems.addView(view)
        }

    }
}