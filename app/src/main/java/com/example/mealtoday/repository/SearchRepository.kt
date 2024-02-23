package com.example.mealtoday.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mealtoday.data.MealList
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class SearchRepository @Inject constructor(
    @Named("mealApi")
    private val mealApi: MealApi
) {

    suspend fun getSearchMeal(searchQuery: String): Response<MealList> {
        val response = mealApi.getSearchMeals(searchQuery)
        if (response.isSuccessful) {
            Log.d("TAG", "SearchMeal 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "SearchMeal 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}