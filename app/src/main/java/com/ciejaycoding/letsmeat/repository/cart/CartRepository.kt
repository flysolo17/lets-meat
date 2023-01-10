package com.ciejaycoding.letsmeat.repository.cart

import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.utils.UiState
import com.firebase.ui.firestore.FirestoreRecyclerOptions

interface CartRepository {
    suspend fun addToCart(uid: String,cart: Cart,result : (UiState<String>) -> Unit)

    suspend fun removeToCart(uid: String,cartID : String)
    fun getAllCart(uid : String ,result : (UiState<List<Cart>>) -> Unit)
    suspend fun incrementQuantity(uid: String,cartAndProduct: CartAndProduct)
    suspend fun decrementQuantity(uid: String,cartAndProduct: CartAndProduct)
    fun addToCheckout(cartAndProduct: CartAndProduct)
    fun removeToCheckOut(cartAndProduct: CartAndProduct)
    fun editToCheckOut(isCheck : Boolean,cart: Cart)
    fun getCheckoutList() : List<CartAndProduct>
    fun checkOut(order : Order, result: (UiState<String>) -> Unit)


}