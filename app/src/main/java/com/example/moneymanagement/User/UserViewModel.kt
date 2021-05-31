package com.example.moneymanagement.User

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.TransactionData.TransactionRepository

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val tag = "UserViewModel"

    init {
        Log.d(tag, "created")
    }

    private var transactionRepository = TransactionRepository(application)
    private var transactions: LiveData<List<TransactionEntity>>? = transactionRepository.getTransactions()

    fun insertTransaction(data: TransactionEntity) {
        transactionRepository.insert(data)
    }

    fun getTransactions(): LiveData<List<TransactionEntity>>? {
        return transactions
    }

    fun deleteTransactions(data: TransactionEntity) {
        transactionRepository.delete(data)
    }

    fun updateTransactions(data: TransactionEntity) {
        transactionRepository.update(data)
    }


}