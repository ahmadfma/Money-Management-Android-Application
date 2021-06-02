package com.example.moneymanagement.User

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.Saldo.SaldoRepository
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.TransactionData.TransactionRepository

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val tag = "UserViewModel"

    init {
        Log.d(tag, "created")
    }

    private var transactionRepository = TransactionRepository(application)
    private var saldoRepository = SaldoRepository(application)
    private var transactions: LiveData<List<TransactionEntity>>? = transactionRepository.getTransactions()
    private var allDateTransactions: LiveData<List<String>>? = transactionRepository.getAllDateTransactions()

    //TRANSACTIONS
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

    fun getAllDateTransactions(): LiveData<List<String>>? {
        return allDateTransactions
    }

    fun getTransactionBasedOnDate(date: String): List<TransactionEntity>? {
        return transactionRepository.getTransactionsBasedOnDate(date)
    }

    fun getLastTransaction(): LiveData<List<TransactionEntity>>?{
        return transactionRepository.getLastTransactions()
    }

    //SALDO
    suspend fun getCurrentSaldo(): Int? {
        return saldoRepository.getCurrentSaldo()
    }

    suspend fun getCurrentPemasukan(): Int? {
        return saldoRepository.getCurrentPemasukan()
    }

    suspend fun getCurrentPengeluaran(): Int? {
        return saldoRepository.getCurrentPengeluaran()
    }

}