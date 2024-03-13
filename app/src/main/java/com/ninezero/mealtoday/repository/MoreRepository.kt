package com.ninezero.mealtoday.repository

import android.util.Log
import com.ninezero.mealtoday.model.BannerList
import com.ninezero.mealtoday.model.BeverageList
import com.ninezero.mealtoday.model.CocktailList
import com.ninezero.mealtoday.model.DrinkList
import com.ninezero.mealtoday.network.CockTailApi
import com.ninezero.mealtoday.network.MealApi
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
        } else {
            Log.d("TAG", "BannerMeal 연결 실패")
        }
        return response
    }

    suspend fun getCocktails(cocktailName: String): Response<CocktailList> {
        val response = cockTailApi.getCocktails(cocktailName)
        if (response.isSuccessful) {
            Log.d("TAG", "CockTail 연결 성공")
        } else {
            Log.d("TAG", "CockTail 연결 실패")
        }
        return response
    }

    suspend fun getDrinks(drinkName: String): Response<DrinkList> {
        val response = cockTailApi.getDrinks(drinkName)
        if (response.isSuccessful) {
            Log.d("TAG", "Drinks 연결 성공")
        } else {
            Log.d("TAG", "Drinks 연결 실패")
        }
        return response
    }

    suspend fun getBeverageInfo(beverageId: String): Response<BeverageList> {
        val response = cockTailApi.getBeverageInfo(beverageId)
        if (response.isSuccessful) {
            Log.d("TAG", "DrinkInfo 연결 성공")
        } else {
            Log.d("TAG", "DrinkInfo 연결 실패")
        }
        return response
    }
}