package com.ciejaycoding.letsmeat.repository.purchases

import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PurchasesRepositoryImpl(val firestore: FirebaseFirestore) : PurchasesRepository {
    private val orderList : MutableList<Order> = mutableListOf()
    override suspend fun getAllPendingOrders(uid: String, result: (UiState<List<Order>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection("Orders")
            .whereEqualTo("clientID",uid)
            .orderBy("date",Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    orderList.clear()
                    for (orders in it.result) {
                        val order = orders.toObject(Order::class.java)
                        orderList.add(order)
                    }
                    result.invoke(UiState.Success(orderList))
                } else {
                    result.invoke(UiState.Failed("Failed Fetching order..."))
                }
            }
    }
}