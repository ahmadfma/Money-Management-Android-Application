package com.example.moneymanagement.User.TransactionData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * From user_transactions")
    fun getTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT date From user_transactions")
    fun getAllDateTransactions(): LiveData<List<String>>

    @Query("SELECT * FROM user_transactions WHERE date LIKE :date")
    suspend fun getTransactionsBasedOnDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM user_transactions ORDER BY id DESC LIMIT :limit")
    fun getLastTransactions(limit: Int): LiveData<List<TransactionEntity>>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(data: TransactionEntity)

    @Update
    suspend fun updateTransaction(data: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(data: TransactionEntity)
}