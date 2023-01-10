package com.ciejaycoding.letsmeat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.repository.purchases.PurchasesRepository
import com.ciejaycoding.letsmeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(private val purchasesRepository: PurchasesRepository) : ViewModel() {
    private val _orders  = MutableLiveData<UiState<List<Order>>>()
    val orders : LiveData<UiState<List<Order>>> get() = _orders
    private val _deleteOrder  = MutableLiveData<UiState<String>>()
    val deleteOrder : LiveData<UiState<String>> get() = _deleteOrder
    fun getAllPendingOrders(uid : String) {
        viewModelScope.launch {
            purchasesRepository.getAllPendingOrders(uid) {
                _orders.value = it
            }
        }
    }
    fun deleteOrder(id : String) {
        viewModelScope.launch {
            purchasesRepository.deleteOrder(id)  {
                _deleteOrder.value = it
            }
        }
    }
}