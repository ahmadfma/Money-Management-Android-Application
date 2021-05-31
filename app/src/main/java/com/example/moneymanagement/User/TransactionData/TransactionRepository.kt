package com.example.moneymanagement.User.TransactionData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TransactionRepository(application: Application) {

    private var transactionDao: TransactionDao?
    private var transactions: LiveData<List<TransactionEntity>>? = null

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        transactionDao = db.transactionDao()
        transactions = transactionDao!!.getTransactions()
    }

    fun getTransactions(): LiveData<List<TransactionEntity>>? {
        return transactions
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