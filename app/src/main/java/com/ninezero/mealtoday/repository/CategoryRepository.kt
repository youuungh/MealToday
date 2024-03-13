package com.ninezero.mealtoday.repository

import android.util.Log
import com.ninezero.mealtoday.model.HotList
import com.ninezero.mealtoday.network.MealApi
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
        } else {
            Log.d("TAG", "Category 연결 실패")
        }
        return response
    }
}