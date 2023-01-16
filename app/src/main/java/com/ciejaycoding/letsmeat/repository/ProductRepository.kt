package com.ciejaycoding.letsmeat.repository

import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.UiState

interface ProductRepository {

    suspend fun getAllProducts(result : (UiState<List<Products>>) -> Unit)
    //suspend fun getProductComments(userList : List<String>,result: (UiState<List<Clients>>) -> Unit)

}