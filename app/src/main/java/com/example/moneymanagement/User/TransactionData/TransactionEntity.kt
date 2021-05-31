package com.example.moneymanagement.User.TransactionData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var type: String,
    var category: String,
    var amount: Int,
    var title: String,
    var date: String
)