package com.example.mealtoday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mealtoday.model.Meal

@Database(
    entities = [Meal::class],
    version = 1
)
@TypeConverters(
    MealConverters::class
)

abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
}