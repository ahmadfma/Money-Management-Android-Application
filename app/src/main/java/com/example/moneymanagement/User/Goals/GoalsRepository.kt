package com.example.moneymanagement.User.Goals

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.moneymanagement.User.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GoalsRepository(application: Application) {

    private val goalsDao: GoalsDao?

    init {
        goalsDao = UserDatabase.getDatabase(application.applicationContext).goalsDao()
    }

    fun getUnReachedGoals(): LiveData<List<GoalsEntity>>? {
        return goalsDao?.getUnReachedGoals()
    }

    fun getReachedGoals(): LiveData<List<GoalsEntity>>? {
        return goalsDao?.getReachedGoals()
    }

    fun insertGoals(goalsEntity: GoalsEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            goalsDao?.insertGoals(goalsEntity)
        }
    }

    fun deleteGoals(goalsEntity: GoalsEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            goalsDao?.deleteGoals(goalsEntity)
        }
    }

    fun updateGoals(goalsEntity: GoalsEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            goalsDao?.updateGoals(goalsEntity)
        }
    }

}