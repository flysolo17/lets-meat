package com.ciejaycoding.letsmeat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Details
import com.ciejaycoding.letsmeat.models.OrderStatus
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.repository.transaction.TransactionRepository
import com.ciejaycoding.letsmeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository) : ViewModel() {
    private val _getAllTransaction = MutableLiveData<UiState<List<Transaction>>>()
    val transactions : LiveData<UiState<List<Transaction>>> get() =  _getAllTransaction

    private val _cancelTransaction = MutableLiveData<UiState<String>>()
    val cancelTransaction : LiveData<UiState<String>> get() =  _cancelTransaction
    fun getTransactions(uid : String) {
        viewModelScope.launch {
            transactionRepository.getAllTransactionByID(uid) {
                _getAllTransaction.value = it
            }
        }
    }
    fun cancelTransaction(
        transaction: Transaction
    ) {
        viewModelScope.launch {
            transactionRepository.cancelTransaction(transaction) {
                _cancelTransaction.value = it
            }
        }

    }
}