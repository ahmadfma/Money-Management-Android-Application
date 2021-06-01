package com.example.moneymanagement.User.Saldo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SaldoDao {

    @Query("SELECT saldo FROM user_saldo")
    fun getCurrentSaldo(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSaldo(saldo: SaldoEntity)
}