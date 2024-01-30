package com.example.mealtoday.network

import com.example.mealtoday.db.Categories
import com.example.mealtoday.db.HotList
import com.example.mealtoday.db.MealList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealList>

    @GET("filter.php")
    suspend fun getHotMeals(@Query("c") categoryName: String): Response<HotList>

    @GET("categories.php")
    suspend fun getCategoriesHomeFragment(): Response<Categories>

    @GET("lookup.php")
    suspend fun getMealInfo(@Query("i") mealId: String): Response<MealList>
}