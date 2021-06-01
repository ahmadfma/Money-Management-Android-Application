package com.example.moneymanagement.User.Saldo

import android.app.Application
import com.example.moneymanagement.User.UserDatabase

class SaldoRepository(application: Application) {

    private val saldoDao: SaldoDao?

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        saldoDao = db.saldoDao()
    }

}