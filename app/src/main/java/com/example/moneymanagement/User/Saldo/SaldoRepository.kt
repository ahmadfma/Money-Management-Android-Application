package com.example.moneymanagement.User.Saldo

import android.app.Application
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SaldoRepository(application: Application) {

    private val saldoDao: SaldoDao?

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        saldoDao = db.saldoDao()
    }

    fun getCurrentSaldo(): Long? {
        var saldo: Long? = 0L
        runBlocking {
            saldo = withContext(this.coroutineContext + Dispatchers.IO) {
                saldoDao?.getCurrentSaldo()
            }
        }
        return saldo
    }

    fun getCurrentPemasukan(): Long? {
        var pemasukan: Long? = 0L
        runBlocking {
            pemasukan = withContext(this.coroutineContext + Dispatchers.IO) {
                saldoDao?.getCurrentPemasukan()
            }
        }
        return pemasukan
    }

    fun getCurrentPengeluaran(): Long? {
        var pengeluaran: Long? = 0L
        runBlocking {
            pengeluaran = withContext(this.coroutineContext + Dispatchers.IO) {
                saldoDao?.getCurrentPengeluaran()
            }
        }
        return pengeluaran
    }

    fun insertUserSaldo(saldo: SaldoEntity) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                saldoDao?.insertSaldo(saldo)
            }
        }
    }

}