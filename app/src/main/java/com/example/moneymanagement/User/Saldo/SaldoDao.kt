package com.example.moneymanagement.User.Saldo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SaldoDao {

    @Query("SELECT saldo FROM user_saldo")
    suspend fun getCurrentSaldo(): Int

    @Query("SELECT pemasukan FROM user_saldo")
    suspend fun getCurrentPemasukan(): Int

    @Query("SELECT pengeluaran FROM user_saldo")
    suspend fun getCurrentPengeluaran(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaldo(saldo: SaldoEntity)
}