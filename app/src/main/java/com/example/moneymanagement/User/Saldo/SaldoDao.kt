package com.example.moneymanagement.User.Saldo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SaldoDao {

    @Query("SELECT saldo FROM user_saldo")
    fun getCurrentSaldo(): LiveData<Long>

    @Query("SELECT pemasukan FROM user_saldo")
    fun getCurrentPemasukan(): LiveData<Long>

    @Query("SELECT pengeluaran FROM user_saldo")
    fun getCurrentPengeluaran(): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaldo(saldo: SaldoEntity)
}