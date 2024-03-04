package com.example.mealtoday.repository

import android.util.Log
import com.example.mealtoday.model.BannerList
import com.example.mealtoday.model.BeverageList
import com.example.mealtoday.model.CocktailList
import com.example.mealtoday.model.DrinkList
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

    suspend fun getBannerMeal(categoryName: String): Response<BannerList> {
        val response = mealApi.getBannerMeals(categoryName)
        if (response.isSuccessful) {
            Log.d("TAG", "BannerMeal 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "BannerMeal 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun getCocktails(cocktailName: String): Response<CocktailList> {
        val response = cockTailApi.getCocktails(cocktailName)
        if (response.isSuccessful) {
            Log.d("TAG", "CockTail 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "CockTail 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun getDrinks(drinkName: String): Response<DrinkList> {
        val response = cockTailApi.getDrinks(drinkName)
        if (response.isSuccessful) {
            Log.d("TAG", "Drinks 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "Drinks 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }

    suspend fun getBeverageInfo(beverageId: String): Response<BeverageList> {
        val response = cockTailApi.getBeverageInfo(beverageId)
        if (response.isSuccessful) {
            Log.d("TAG", "DrinkInfo 연결 성공")
            Log.d("TAG", response.code().toString())
        } else {
            Log.d("TAG", "DrinkInfo 연결 실패")
            Log.d("TAG", response.code().toString())
        }
        return response
    }
}