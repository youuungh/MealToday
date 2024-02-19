package com.example.mealtoday.repository

import android.util.Log
import com.example.mealtoday.data.DrinkList
import com.example.mealtoday.data.SliderList
import com.example.mealtoday.network.CockTailApi
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class MoreRepository @Inject constructor(
    @Named("mealApi")
    private val mealApi: MealApi,
    @Named("cockTailApi")
    private val cockTailApi: CockTailApi
) {

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

    suspend fun getDrinks(drinkName: String): Response<DrinkList> {
        val response = cockTailApi.getDrinks(drinkName)
        if (response.isSuccessful) {
            Log.d("TAG", "CockTail 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "CockTail 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}