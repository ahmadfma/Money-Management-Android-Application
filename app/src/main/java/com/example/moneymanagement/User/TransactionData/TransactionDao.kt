package com.example.moneymanagement.User.TransactionData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * From user_transactions")
    fun getTransactions(): LiveData<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(data: TransactionEntity)

    @Update
    suspend fun updateTransaction(data: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(data: TransactionEntity)
}