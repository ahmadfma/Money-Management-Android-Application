package com.example.moneymanagement.User.TransactionData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * From user_transactions")
    fun getTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT date From user_transactions ORDER BY id DESC")
    fun getAllDateTransactions(): LiveData<List<String>>

    @Query("SELECT * FROM user_transactions WHERE date LIKE :date ORDER BY id DESC")
    suspend fun getTransactionsBasedOnDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM user_transactions ORDER BY id DESC LIMIT :limit")
    fun getLastTransactions(limit: Int): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM user_transactions WHERE type = :type")
    fun getTotalAmount(type: String): Long

    @Query("SELECT SUM(amount) FROM user_transactions WHERE category = :category AND type = :type")
    fun getTotalAmountByCategory(category: String, type: String): Long

    @Query("SELECT SUM(amount) FROM user_transactions WHERE date LIKE :month AND type = :type")
    fun getTotalAmountByMonth(month: String, type: String): Long

    @Query("SELECT SUM(amount) FROM user_transactions WHERE date LIKE :month AND type = :type AND category = :category")
    fun getTotalAmountByMonthAndCategory(month: String, type: String, category: String): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(data: TransactionEntity)

    @Update
    suspend fun updateTransaction(data: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(data: TransactionEntity)
}