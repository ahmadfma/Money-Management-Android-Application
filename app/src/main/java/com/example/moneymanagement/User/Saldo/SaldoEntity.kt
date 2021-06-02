package com.example.moneymanagement.User.Saldo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_saldo")
data class SaldoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    var saldo: Long,
    var pemasukan: Long,
    var pengeluaran: Long
)
