package com.ciejaycoding.letsmeat.repository.transaction


import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.UiState

interface TransactionRepository {
    suspend fun  getAllTransactionByID(uid : String,result: (UiState<List<Transaction>>) -> Unit)
    suspend fun cancelTransaction(transaction: Transaction,result: (UiState<String>) -> Unit)
}