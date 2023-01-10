package com.ciejaycoding.letsmeat.repository.purchases

import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.utils.UiState

interface PurchasesRepository {
    suspend fun getAllPendingOrders(uid : String,result: (UiState<List<Order>>) -> Unit)
    suspend fun deleteOrder(id : String,result: (UiState<String>) -> Unit)
}