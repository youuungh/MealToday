package com.example.mealtoday.repository

import android.util.Log
import com.example.mealtoday.data.HotList
import com.example.mealtoday.network.MealApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class CategoryRepository @Inject constructor(
    @Named("mealApi")
    private val mealApi: MealApi
) {

    suspend fun getCategory(categoryName: String): Response<HotList> {
        val response = mealApi.getCategory(categoryName)
        if (response.isSuccessful) {
            Log.d("TAG", "Category 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "Category 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}