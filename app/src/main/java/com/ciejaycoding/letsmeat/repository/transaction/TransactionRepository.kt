package com.ciejaycoding.letsmeat.repository.transaction


import com.ciejaycoding.letsmeat.models.Comments
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.UiState

interface TransactionRepository {
    suspend fun  getAllTransactionByID(uid : String,result: (UiState<List<Transaction>>) -> Unit)
    suspend fun cancelTransaction(transaction: Transaction,result: (UiState<String>) -> Unit)
    suspend fun getTransactionByID(id : String,result: (UiState<Transaction>) -> Unit)
    suspend fun getToRateTransactoions(uid : String ,result: (UiState<List<Transaction>>) -> Unit)
    suspend fun addComment(productList : List<String>, comments: Comments, result: (UiState<String>) -> Unit)
    suspend fun getAllTransactions(result: (UiState<List<Transaction>>) -> Unit)
}