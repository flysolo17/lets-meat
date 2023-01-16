package com.ciejaycoding.letsmeat.repository.purchases

import com.ciejaycoding.letsmeat.models.Comments
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.utils.ORDER_TABLE
import com.ciejaycoding.letsmeat.utils.PRODUCTS_TABLE
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.firestore.*

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

    override suspend fun deleteOrder(id: String,result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(ORDER_TABLE)
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Order cancelled!"))
                } else {
                    result.invoke(UiState.Failed("Order not deleted!"))
                }
            }
    }


}