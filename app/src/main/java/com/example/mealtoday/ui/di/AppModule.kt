package com.example.mealtoday.ui.di

import android.app.Application
import androidx.room.Room
import com.example.mealtoday.db.MealDatabase
import com.example.mealtoday.network.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi(): MealApi = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MealApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MealDatabase =
        Room.databaseBuilder(
            app,
            MealDatabase::class.java,
            "meal.db"
        ).build()
}