package com.example.moneymanagement.User.Goals

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GoalsDao {

    @Query("SELECT * FROM user_goals WHERE reached = 'true'")
    fun getReachedGoals(): LiveData<List<GoalsEntity>>

    @Query("SELECT * FROM user_goals WHERE reached = 'false'")
    fun getUnReachedGoals(): LiveData<List<GoalsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGoals(goalsEntity: GoalsEntity)

    @Update
    fun updateGoals(goalsEntity: GoalsEntity)

    @Delete
    fun deleteGoals(goalsEntity: GoalsEntity)
}