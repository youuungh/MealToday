package com.ninezero.mealtoday.repository

import android.util.Log
import com.ninezero.mealtoday.model.MealList
import com.ninezero.mealtoday.network.MealApi
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
        } else {
            Log.d("TAG", "SearchMeal 연결 실패")
        }
        return response
    }
}