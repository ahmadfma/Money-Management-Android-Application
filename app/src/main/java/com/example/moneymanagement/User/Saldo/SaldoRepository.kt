package com.example.moneymanagement.User.Saldo

import android.app.Application
import androidx.lifecycle.LiveData
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

    fun getCurrentSaldo(): LiveData<Int>? {
        return saldoDao?.getCurrentSaldo()
    }

    fun getCurrentPemasukan(): LiveData<Int>? {
        return saldoDao?.getCurrentPemasukan()
    }

    fun getCurrentPengeluaran(): LiveData<Int>? {
        return saldoDao?.getCurrentPengeluaran()
    }

}