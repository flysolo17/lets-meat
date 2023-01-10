package com.ciejaycoding.letsmeat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.repository.cart.CartRepository
import com.ciejaycoding.letsmeat.utils.UiState
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val  cartRepository: CartRepository) : ViewModel(){

    private var _addToCart = MutableLiveData<UiState<String>>()
    val addToCart : LiveData<UiState<String>> get() = _addToCart


    private var _cartList = MutableLiveData<UiState<List<Cart>>>()
    val cart : LiveData<UiState<List<Cart>>> get() = _cartList

    private var _checkoutList = MutableLiveData<List<CartAndProduct>>()
    val checkout : LiveData<List<CartAndProduct>> get() = _checkoutList

    private var _checkOutOrder= MutableLiveData<UiState<String>>()
    val checkoutOrder : LiveData<UiState<String>> get() = _checkOutOrder

    fun addToCart(uid: String,cart: Cart) {
        viewModelScope.launch {
            cartRepository.addToCart(uid,cart) {
                _addToCart.value = it
            }
        }
    }
    fun increment(uid: String,cartAndProduct: CartAndProduct) {
        viewModelScope.launch {
            cartRepository.incrementQuantity(uid,cartAndProduct)
        }
    }
    fun decrement(uid: String,cartAndProduct: CartAndProduct) {
        viewModelScope.launch {
            cartRepository.decrementQuantity(uid,cartAndProduct)
        }
    }

    fun getAllCart(uid : String) {
        viewModelScope.launch {
            cartRepository.getAllCart(uid) {
                _cartList.value = it
            }
        }
    }

    fun addToCheckout(cartAndProduct: CartAndProduct) {
       cartRepository.addToCheckout(cartAndProduct).also {
           _checkoutList.value = cartRepository.getCheckoutList()
       }
    }

    fun removeToCheckout(cartAndProduct: CartAndProduct) {
        cartRepository.removeToCheckOut(cartAndProduct).also {
            _checkoutList.value = cartRepository.getCheckoutList()
        }
    }
    fun checkoutOrder(order: Order) {
        viewModelScope.launch {
            cartRepository.checkOut(order) {
                _checkOutOrder.value = it
            }
        }
    }
    fun removeFromCart(uid : String , cartID : String) {
        viewModelScope.launch {
            cartRepository.removeToCart(uid, cartID)
        }
    }
}