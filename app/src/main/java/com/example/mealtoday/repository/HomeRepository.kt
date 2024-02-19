package com.example.mealtoday.repository

import android.util.Log
import com.example.mealtoday.data.Categories
import com.example.mealtoday.data.HotList
import com.example.mealtoday.data.MealList
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class HomeRepository @Inject constructor(
    @Named("mealApi")
    private val mealApi: MealApi
) {
    suspend fun getRandomMeal(): Response<MealList> {
        val response = mealApi.getRandomMeal()
        if (response.isSuccessful) {
            Log.d("TAG", "RandomMeal 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "RandomMeal 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun getHotMeals(categoryName: String): Response<HotList> {
        val response = mealApi.getHotMeals(categoryName)
        if (response.isSuccessful) {
            Log.d("TAG", "HotMeal 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "HotMeal 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun getCategoriesHome(): Response<Categories> {
        val response = mealApi.getCategoriesHomeFragment()
        if (response.isSuccessful) {
            Log.d("TAG", "Categories 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "Categories 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}