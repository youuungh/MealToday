package com.ninezero.mealtoday.repository

import android.util.Log
import com.ninezero.mealtoday.model.Categories
import com.ninezero.mealtoday.model.HotList
import com.ninezero.mealtoday.model.MealList
import com.ninezero.mealtoday.network.MealApi
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
        } else {
            Log.d("TAG", "RandomMeal 연결 실패")
        }
        return response
    }

    suspend fun getHotMeals(categoryName: String): Response<HotList> {
        val response = mealApi.getHotMeals(categoryName)
        if (response.isSuccessful) {
            Log.d("TAG", "HotMeal 연결 성공")
        } else {
            Log.d("TAG", "HotMeal 연결 실패")
        }
        return response
    }

    suspend fun getCategoriesHome(): Response<Categories> {
        val response = mealApi.getCategoriesHomeFragment()
        if (response.isSuccessful) {
            Log.d("TAG", "Categories 연결 성공")
        } else {
            Log.d("TAG", "Categories 연결 실패")
        }
        return response
    }
}