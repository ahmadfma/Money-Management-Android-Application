package com.example.moneymanagement.User.Saldo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SaldoRepository(application: Application) {

    private val saldoDao: SaldoDao?

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        saldoDao = db.saldoDao()
    }

    fun getCurrentSaldo(): LiveData<Long>? {
        return saldoDao?.getCurrentSaldo()
    }

    fun getCurrentPemasukan(): LiveData<Long>? {
        return saldoDao?.getCurrentPemasukan()
    }

    fun getCurrentPengeluaran(): LiveData<Long>? {
        return saldoDao?.getCurrentPengeluaran()
    }

    fun insertUserSaldo(saldo: SaldoEntity) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                saldoDao?.insertSaldo(saldo)
            }
        }
    }

}