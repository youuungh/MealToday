package com.example.mealtoday.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealtoday.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInfo WHERE idMeal = :id")
    fun getFavoriteMeal(id: String?): LiveData<List<Meal>>

    @Query("SELECT * FROM mealInfo")
    fun getAllFavoriteMeal(): Flow<List<Meal>>
}