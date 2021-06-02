package com.example.moneymanagement.User.Saldo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_saldo")
data class SaldoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var saldo: Int,
    var pemasukan: Int,
    var pengeluaran: Int
)
