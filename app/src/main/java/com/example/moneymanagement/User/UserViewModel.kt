package com.example.moneymanagement.User

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.Goals.GoalsRepository
import com.example.moneymanagement.User.Saldo.SaldoEntity
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
    private var goalsRepository = GoalsRepository(application)

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

    fun getLastTransaction(): List<TransactionEntity>? {
        return transactionRepository.getLastTransactions()
    }

    fun getTotalAmount(type: String): Long? {
        return transactionRepository.getTotalAmount(type)
    }

    fun getTotalAmountByCategory(category: String, type: String): Long? {
        return transactionRepository.getTotalAmountByCategory(category, type)
    }

    fun getTotalAmountByMonth(month: String, type: String): Long? {
        return transactionRepository.getTotalAmountByMonth(month, type)
    }

    fun getTotalAmountByMonthAndCategory(month: String, type: String, category: String): Long? {
        return transactionRepository.getTotalAmountByMonthAndCategory(month, type, category)
    }

    //SALDO
    fun getCurrentSaldo(): Long? {
        return saldoRepository.getCurrentSaldo()
    }

    fun getCurrentPemasukan(): Long? {
        return saldoRepository.getCurrentPemasukan()
    }

    fun getCurrentPengeluaran(): Long? {
        return saldoRepository.getCurrentPengeluaran()
    }

    fun insertUserSaldo(saldo: SaldoEntity) {
        saldoRepository.insertUserSaldo(saldo)
    }

    //GOALS
    fun getReachedGoals(): LiveData<List<GoalsEntity>>? {
        return goalsRepository.getReachedGoals()
    }

    fun getUnReachedGoals(): LiveData<List<GoalsEntity>>? {
        return goalsRepository.getUnReachedGoals()
    }

    fun insertGoals(goalsEntity: GoalsEntity) {
        goalsRepository.insertGoals(goalsEntity)
    }

    fun updateGoals(goalsEntity: GoalsEntity) {
        goalsRepository.updateGoals(goalsEntity)
    }

    fun deleteGoals(goalsEntity: GoalsEntity) {
        goalsRepository.deleteGoals(goalsEntity)
    }

}