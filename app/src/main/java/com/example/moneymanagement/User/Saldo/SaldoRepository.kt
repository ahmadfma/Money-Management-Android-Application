package com.example.moneymanagement.User.Saldo

import android.app.Application
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SaldoRepository(application: Application) {

    private val saldoDao: SaldoDao?

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        saldoDao = db.saldoDao()
    }

    suspend fun getCurrentSaldo(): Int? {
        return saldoDao?.getCurrentSaldo()
    }

    suspend fun getCurrentPemasukan(): Int? {
        return saldoDao?.getCurrentPemasukan()
    }

    suspend fun getCurrentPengeluaran(): Int? {
        return saldoDao?.getCurrentPengeluaran()
    }

}