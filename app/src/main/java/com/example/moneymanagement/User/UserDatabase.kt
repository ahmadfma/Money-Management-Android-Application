package com.example.moneymanagement.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneymanagement.User.Goals.GoalsDao
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.Saldo.SaldoDao
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.TransactionData.TransactionDao
import com.example.moneymanagement.User.TransactionData.TransactionEntity

@Database(entities = [TransactionEntity::class, SaldoEntity::class, GoalsEntity::class], exportSchema = false, version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun saldoDao(): SaldoDao
    abstract fun goalsDao(): GoalsDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null
        private const val DB_NAME = "user_database"

        fun getDatabase(context: Context): UserDatabase {
            val tempinstance = INSTANCE
            if(tempinstance != null) {
                return tempinstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}