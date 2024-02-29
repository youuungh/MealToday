package com.example.mealtoday.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mealtoday.data.Meal
import com.example.mealtoday.data.MealList
import com.example.mealtoday.db.MealDatabase
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class MealRepository @Inject constructor(
    @Named("mealApi")
    private val mealApi: MealApi,
    db: MealDatabase
) {
    private val database = db.mealDao()

    suspend fun getMealInfo(mealId: String): Response<MealList> {
        val response = mealApi.getMealInfo(mealId)
        if (response.isSuccessful) {
            Log.d("TAG", "MealInfo 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "MealInfo 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun upsertMeal(meal: Meal) {
        database.upsertMeal(meal)
    }

    suspend fun deleteMeal(meal: Meal) {
        database.deleteMeal(meal)
    }

    fun getFavoriteMeal(id: String?): LiveData<List<Meal>> {
        return database.getFavoriteMeal(id)
    }

    val getAllFavoriteMeal = database.getAllFavoriteMeal()
}