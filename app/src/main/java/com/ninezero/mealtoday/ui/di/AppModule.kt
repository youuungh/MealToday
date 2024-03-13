package com.ninezero.mealtoday.ui.di

import android.app.Application
import androidx.room.Room
import com.ninezero.mealtoday.db.MealDatabase
import com.ninezero.mealtoday.network.CockTailApi
import com.ninezero.mealtoday.network.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @Named("mealApi")
    fun provideMealApi(): MealApi = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MealApi::class.java)

    @Provides
    @Singleton
    @Named("cockTailApi")
    fun provideCockTailApi(): CockTailApi = Retrofit.Builder()
        .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CockTailApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MealDatabase =
        Room.databaseBuilder(
            app,
            MealDatabase::class.java,
            "meal.db"
        ).build()
}