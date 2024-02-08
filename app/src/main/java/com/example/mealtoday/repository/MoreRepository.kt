package com.example.mealtoday.repository

import android.util.Log
import com.example.mealtoday.data.SliderList
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject

class MoreRepository @Inject constructor(private val mealApi: MealApi) {

    suspend fun getSliderMeal(categoryName: String): Response<SliderList> {
        val response = mealApi.getSliderMeals(categoryName)
        if (response.isSuccessful) {
            Log.d("TAG", "SliderMeal 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "SliderMeal 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}