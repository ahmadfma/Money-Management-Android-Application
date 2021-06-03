package com.example.moneymanagement.User.Goals

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalsDao {

    @Query("SELECT * FROM user_goals WHERE reached = 'true'")
    fun getReachedGoals(): LiveData<List<GoalsEntity>>

    @Query("SELECT * FROM user_goals WHERE reached = 'false'")
    fun getUnReachedGoals(): LiveData<List<GoalsEntity>>

    @Update
    fun updateGoals(goalsEntity: GoalsEntity)

    @Delete
    fun deleteGoals(goalsEntity: GoalsEntity)
}