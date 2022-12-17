package com.ciejaycoding.letsmeat.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.OrderItems
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
const val PRODUCTS_TABLE = "Products"
const val CLIENTS_TABLE = "Clients"
const val CLIENTS_CART = "Cart"
const val ORDER_TABLE = "Orders"
const val PROFILE_STORAGE = "profiles"
fun computePrice(cartAndProduct: CartAndProduct) : Float {
    return cartAndProduct.cart!!.quantity * cartAndProduct.products!!.price
}
fun formatPrice(number: Float) : String{
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = '\''
    symbols.decimalSeparator = ','
    val decimalFormat = DecimalFormat("₱ #,###.00")
    return decimalFormat.format(number)
}

fun itemCount(productList : List<CartAndProduct>) : Int{
    return productList.sumOf { it.cart!!.quantity }
}
fun computeProductTotal(productList : List<CartAndProduct>) : Int {
    return productList.sumOf { it.cart!!.quantity * it.products!!.price.toInt()}
}
fun computeShippingFee(productList : List<CartAndProduct>) : Int {
    val totalWeight = productList.sumOf { it.products!!.weight * it.cart!!.quantity}
    return totalWeight * 20
}

fun countTotalWeight(productList: List<CartAndProduct>) : Int{
    return productList.sumOf { it.products!!.weight * it.cart!!.quantity}
}
fun generateOrderNumber() : String {
    val str = "0123456789"
    var orderNumber = ""
    for (i in 1..20) {
        orderNumber += str.random()
    }
    return orderNumber;
}
//TODO: get the file extension of the file
fun Activity.getFileExtension(uri: Uri): String? {
    val cR = this.contentResolver
    val mime = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cR.getType(uri))
}
fun computeItemPrice(items: OrderItems) : Float {
    return items.quantity!! * items.originalPrice!!
}
fun orderTotal(order : Order) : Int {
    val shipping = order.items!!.sumOf { it.weight!! * it.quantity!! } * 20
    val itemTotal = order.items.sumOf { it.quantity!! * it.originalPrice!!.toInt() }
    return itemTotal + shipping
}

fun countOrder(order: Order) : String {
    val count=order.items!!.sumOf { it.quantity!! }
    return if (count == 1) {
        "$count item"
    } else "$count items"
}