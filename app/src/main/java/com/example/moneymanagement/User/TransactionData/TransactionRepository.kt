package com.example.moneymanagement.User.TransactionData

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.*

class TransactionRepository(application: Application) {

    private var transactionDao: TransactionDao?
    private var transactions: LiveData<List<TransactionEntity>>? = null
    private var allTransactionsDate: LiveData<List<String>>? = null

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        transactionDao = db.transactionDao()
        transactions = transactionDao!!.getTransactions()
        allTransactionsDate = transactionDao!!.getAllDateTransactions()
    }

    fun getTransactions(): LiveData<List<TransactionEntity>>? {
        return transactions
    }

    fun getAllDateTransactions(): LiveData<List<String>>? {
        return allTransactionsDate
    }

    fun getTransactionsBasedOnDate(date: String): List<TransactionEntity>? {
        val list: List<TransactionEntity>?
        runBlocking {
            list = this.async(Dispatchers.IO) {
                val regex = "%$date%"
                Log.d("TransactionRepo", "getTransactionsBasedOnDate, regex = $regex")
                transactionDao?.getTransactionsBasedOnDate(regex)
            }.await()
            Log.d("TransactionRepo", "getTransactionsBasedOnDate, Size = ${list?.size}")
        }
        Log.d("TransactionRepo", "getTransactionsBasedOnDate, Size = ${list?.size}")
        return list
    }

    fun getLastTransactions(): List<TransactionEntity>? {
        val list: List<TransactionEntity>?
        runBlocking {
            list = this.async(Dispatchers.IO) {
                transactionDao?.getLastTransactions(3)
            }.await()
        }
        return list
    }

    fun getTotalAmount(type: String): Long? {
        return transactionDao?.getTotalAmount(type)
    }

    fun getTotalAmountByCategory(category: String, type: String): Long? {
        var saldo: Long? = 0
        runBlocking {
            saldo = this.async(Dispatchers.IO) {
                transactionDao?.getTotalAmountByCategory(category, type)
            }.await()
        }
        return saldo
    }

    fun insert(data: TransactionEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            transactionDao?.insertTransaction(data)
        }
    }

    fun delete(data: TransactionEntity) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                transactionDao?.deleteTransaction(data)
            }
        }
    }

    fun update(data: TransactionEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            transactionDao?.updateTransaction(data)
        }
    }

}