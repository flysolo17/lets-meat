package com.ciejaycoding.letsmeat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciejaycoding.letsmeat.models.Comments
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

    private val _getTransactionByID = MutableLiveData<UiState<Transaction>>()
    val getTransactionByID : LiveData<UiState<Transaction>> get() =  _getTransactionByID

    private val _rateProduct = MutableLiveData<UiState<String>>()
    val rate : LiveData<UiState<String>> get() =  _rateProduct

    private val _rateList = MutableLiveData<UiState<List<Transaction>>>()
    val rateList : LiveData<UiState<List<Transaction>>> get() =  _rateList
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
    fun getTransactionByID(id : String) {
        viewModelScope.launch {
            transactionRepository.getTransactionByID(id) {
                _getTransactionByID.postValue(it)
            }
        }
    }
   fun addComment(productList : List<String>, comments: Comments) {
       viewModelScope.launch {
           transactionRepository.addComment(productList,comments) {
               _rateProduct.postValue(it)
           }
       }
   }
    fun getToRateList(uid : String) {
        viewModelScope.launch{
            transactionRepository.getToRateTransactoions(uid) {
                _rateList.postValue(it)
            }
        }
    }
    fun getAllTransactions() {
        viewModelScope.launch{
            transactionRepository.getAllTransactions {
                _getAllTransaction.postValue(it)
            }
        }
    }
}