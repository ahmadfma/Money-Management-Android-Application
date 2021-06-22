package com.example.moneymanagement.User.Saldo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SaldoDao {

    @Query("SELECT saldo FROM user_saldo")
    fun getCurrentSaldo(): Long?

    @Query("SELECT pemasukan FROM user_saldo")
    fun getCurrentPemasukan(): Long?

    @Query("SELECT pengeluaran FROM user_saldo")
    fun getCurrentPengeluaran(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaldo(saldo: SaldoEntity)
}