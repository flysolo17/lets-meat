package com.ciejaycoding.letsmeat

import com.ciejaycoding.letsmeat.models.CartAndProduct

public class OrderCalculator (val cartAndProduct: List<CartAndProduct>){
    fun computeProductTotal() : Int {
        return cartAndProduct.sumOf { it.cart!!.quantity * it.products!!.price.toInt()}
    }
    fun computeTotalTax() : Int {
        return 12 * computeProductTotal() / 100
    }
    fun computeTotalWithOutTax(amount : Int) : Int {
        return (100-12) * amount / 100;
    }
    fun computeOrderTotal() {

    }
    fun computeShipping(weight : Int): Int {
        var shipping = 0;
        shipping = if (weight <= 20) {
            150;
        } else if (weight in 21..40) {
            300;
        } else {
            if (weight in 41..59) {
                400
            } else {
                500;
            }
        }
        return shipping;
    }
}