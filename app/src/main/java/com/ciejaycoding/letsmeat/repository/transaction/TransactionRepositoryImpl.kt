package com.ciejaycoding.letsmeat.repository.transaction

import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.Details
import com.ciejaycoding.letsmeat.models.OrderStatus
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.TRANSACTION_TABLE
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class TransactionRepositoryImpl(private val firestore: FirebaseFirestore) : TransactionRepository {
    private val transactionList  = mutableListOf<Transaction>()
    override suspend fun getAllTransactionByID(
        uid: String,
        result: (UiState<List<Transaction>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(TRANSACTION_TABLE)
            .whereEqualTo("clientID",uid)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Failed(it.message!!))
                }
                value?.let {
                    for(snapshot in it.documents) {
                        val transaction  = snapshot.toObject(Transaction::class.java)
                        transaction?.let {
                            transactionList.add(transaction)
                        }
                    }
                    result.invoke(UiState.Success(transactionList))
                }

            }

    }

     override suspend fun cancelTransaction(
        transaction: Transaction,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(TRANSACTION_TABLE)
            .document(transaction.id!!)
            .set(transaction)

            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success(transaction.order?.id!!))
                } else {
                    result.invoke(UiState.Failed("Failed Updating Transaction"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failed(it.message!!))
            }
    }
}