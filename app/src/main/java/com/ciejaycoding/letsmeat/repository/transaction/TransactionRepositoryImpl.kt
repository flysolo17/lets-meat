package com.ciejaycoding.letsmeat.repository.transaction

import com.ciejaycoding.letsmeat.models.*
import com.ciejaycoding.letsmeat.utils.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TransactionRepositoryImpl(private val firestore: FirebaseFirestore) : TransactionRepository {
    private val transactionList  = mutableListOf<Transaction>()
    override suspend fun getAllTransactionByID(
        uid: String,
        result: (UiState<List<Transaction>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(TRANSACTION_TABLE)
            .whereEqualTo("clientID",uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    transactionList.clear()
                    for(snapshot in it.result) {
                        val transaction  = snapshot.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                    result.invoke(UiState.Success(transactionList))
                } else {
                    result.invoke(UiState.Failed("Failed to load orders"))
                }
            }
    }
/*
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
                    transactionList.clear()
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
*/

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

    override suspend fun getTransactionByID(id: String, result: (UiState<Transaction>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(TRANSACTION_TABLE)
            .document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val transaction =it.result.toObject(Transaction::class.java)
                    transaction?.let {
                        result.invoke(UiState.Success(it))
                    }
                } else {
                    result.invoke(UiState.Failed("Failed Getting Transaction"))
                }
            }
    }

    override suspend fun getToRateTransactoions(
        uid: String,
        result: (UiState<List<Transaction>>) -> Unit
    ) {
        val rate  = mutableListOf<Transaction>()
        result.invoke(UiState.Loading)
//            .whereGreaterThan("date", startOfDay(System.currentTimeMillis()))
//            .whereLessThan("date", endOfDay(System.currentTimeMillis()))
        firestore.collection(TRANSACTION_TABLE)
            .whereEqualTo("clientID",uid)
            .orderBy("date",Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        val transaction = document.toObject(Transaction::class.java)
                        if (transaction.status == OrderStatus.COMPLETED) {
                            rate.add(transaction)
                        }
                    }
                    result.invoke(UiState.Success(rate))
                } else {
                    result.invoke(UiState.Failed("Failed to get Transactions"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failed(it.message!!))
            }
    }

    override suspend fun addComment(productList : List<String>, comments: Comments, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        val refList : MutableList<DocumentReference> = mutableListOf()
        productList.map {
            refList.add(firestore.collection(PRODUCTS_TABLE).document(it))
        }
        val writeBatch = firestore.batch()
        refList.map {
            writeBatch.update(it,"comments", FieldValue.arrayUnion(comments))
        }
        writeBatch.commit().addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(UiState.Success("Successfully Rated!"))
            } else {
                result.invoke(UiState.Failed("Failed to rate"))
            }
        }.addOnFailureListener {
            result.invoke(UiState.Failed(it.message!!))
        }
    }

    override suspend fun getAllTransactions(result: (UiState<List<Transaction>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(TRANSACTION_TABLE)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    transactionList.clear()
                    for(snapshot in it.result) {
                        val transaction  = snapshot.toObject(Transaction::class.java)
                        transactionList.add(transaction)
                    }
                    result.invoke(UiState.Success(transactionList))
                } else {
                    result.invoke(UiState.Failed("Failed to load orders"))
                }
            }
    }
}