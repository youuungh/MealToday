package com.ninezero.mealtoday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ninezero.mealtoday.model.Meal

@Database(
    entities = [Meal::class],
    version = 1
)
@TypeConverters(
    MealConverters::class
)

abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao

//    companion object {
//
//        @Volatile
//        private var INSTANCE: MealDatabase? = null
//        fun getDatabase(
//            context: Context
//        ): MealDatabase {
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                MealDatabase::class.java,
//                "meal-database")
//                .fallbackToDestructiveMigration()
//                .build()
//            INSTANCE = instance
//            return instance
//        }
//    }
}
