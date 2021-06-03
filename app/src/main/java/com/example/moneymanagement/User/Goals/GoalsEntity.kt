package com.example.moneymanagement.User.Goals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var reached: Boolean,
    var category: String,
    var amount: Long,
    var note: String,
)