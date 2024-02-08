package com.example.mealtoday.network

import com.example.mealtoday.data.Categories
import com.example.mealtoday.data.HotList
import com.example.mealtoday.data.MealList
import com.example.mealtoday.data.SliderList
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

    @GET("filter.php")
    suspend fun getCategory(@Query("c") categoryName: String): Response<HotList>

    @GET("filter.php")
    suspend fun getSliderMeals(@Query("a") categoryName: String): Response<SliderList>
}