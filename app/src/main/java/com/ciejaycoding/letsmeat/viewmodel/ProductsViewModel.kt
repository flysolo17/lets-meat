package com.ciejaycoding.letsmeat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.repository.ProductRepository
import com.ciejaycoding.letsmeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableLiveData<UiState<List<Products>>>()
    val products : LiveData<UiState<List<Products>>>
    get() = _products

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts {
                _products.value = it
            }
        }

    }
}