package com.ciejaycoding.letsmeat.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap
import com.ciejaycoding.letsmeat.models.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
const val PRODUCTS_TABLE = "Products"
const val GCASH = "Payments"
const val CLIENTS_TABLE = "Clients"
const val CLIENTS_CART = "Cart"
const val MESSAGE_TABLE= "Messages"
const val STAFF_TABLE = "Staff"
const val ORDER_TABLE = "Orders"
const val PROJECT_ID = "OjwzBl5Fl2bZF0GOX2ltbi6QUkF2"
const val PROFILE_STORAGE = "profiles"
const val TRANSACTION_TABLE = "Transactions"
fun computePrice(cartAndProduct: CartAndProduct) : Float {
    return cartAndProduct.cart!!.quantity * cartAndProduct.products!!.price
}

fun formatPrice(number: Float) : String{
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = '\''
    symbols.decimalSeparator = ','
    val decimalFormat = DecimalFormat("₱ #,##0.00")
    return decimalFormat.format(number)
}

fun formatWeight(number: Float) : String{
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = '\''
    symbols.decimalSeparator = ','
    val decimalFormat = DecimalFormat("#,##0.00")
    return decimalFormat.format(number)
}


fun itemCount(productList : List<CartAndProduct>) : Int{
    return productList.sumOf { it.cart!!.quantity }
}
fun computeProductTotal(productList : List<CartAndProduct>) : Int {
    return productList.sumOf { it.cart!!.quantity * it.products!!.price.toInt()}
}
fun countTotalWeight(productList: List<CartAndProduct>) : Double {
    var weight = 0.00
    productList.map {
        weight +=  convertToKilograms(it.products!!.weight.toDouble(), it.products.weightType) * it.cart!!.quantity
    }
    return weight
}
fun convertToKilograms(value: Double, unit: String): Double {
    val kilograms: Double = when (unit.lowercase(Locale.ROOT)) {
        "g", "gram", "grams", "ml", "milliliter", "milliliters" -> value / 1000
        "l", "liter", "liters" ,"kg" -> value
        else -> 1.00
    }
    return kilograms
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
    var weight = 0.00
    order.items!!.map {
        weight +=  convertToKilograms(it.weight!!.toDouble(),it.weightType!!) * it.quantity!!
    }
    val type = if (order.orderType == OrderType.DELIVER) 0 else 1
    val shipping = computeShipping(weight,type)
    val itemTotal = order.items.sumOf { it.quantity!! * it.originalPrice!!.toInt() }
    return (itemTotal + shipping)
}

fun countOrder(order: Order) : String {
    val count=order.items!!.sumOf { it.quantity!! }
    return if (count == 1) {
        "$count item"
    } else "$count items"
}
fun dateFormat(timestamp : Long ) : String {
    val dateFormated = SimpleDateFormat("dd MMM")
    return dateFormated.format(Date(timestamp))
}
fun timeFormat(timestamp : Long ) : String {
    val dateFormated = SimpleDateFormat("hh:mm aa")
    return dateFormated.format(Date(timestamp))
}

fun startOfDay(timestamp: Long): Long {
    val cal = Calendar.getInstance()
    val date = Date(timestamp)
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 1
    return cal.timeInMillis
}

fun endOfDay(timestamp: Long): Long {
    val cal = Calendar.getInstance()
    val date = Date(timestamp)
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 23
    cal[Calendar.MINUTE] = 59
    cal[Calendar.SECOND] = 59
    return cal.timeInMillis
}

fun computeTotalTax(amount : Int) : Int {
    return 12 * amount / 100
}
fun computeTotalWithOutTax(amount : Int) : Int {
    return (100-12) * amount / 100;
}
fun getCommentMedian(comments : List<Comments>) : Float{
    var sum = 0f
    comments.map {
        sum += it.rating!!
    }
    return sum / comments.size
}
fun getRatingSum(comments: List<Comments>) : Float {
    var sum = 0f
    comments.map {
        sum += it.rating!!
    }
    return sum
}
fun getItemSoldTotal(productID : String,transactionList : List<Transaction>) : Int {
    var count = 0
    transactionList.map {  transaction ->
        transaction.order?.items!!.map {
            if(it.id == productID) {
                count += it.quantity!!
            }
        }
    }
    return count
}
fun computeOrderWeight(orderItems: List<OrderItems>) : Float {
    var weight = 0f
    orderItems.map {
        weight = it.weight!! * it.quantity!!
    }
    return weight
}
fun computeShipping(weight : Double,type : Int): Int {
    var shipping = 0;
    if (type == 0) {
        shipping = if (weight < 75.00) {
            50
        } else {
            100
        }
    }
    return shipping;
}

fun getOrderType(order: Order) : Int{
    return  if (order.orderType == OrderType.DELIVER) {
        0
    } else {
        1
    }
}