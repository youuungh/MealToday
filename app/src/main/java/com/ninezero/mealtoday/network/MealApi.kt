package com.ninezero.mealtoday.network

import com.ninezero.mealtoday.model.Categories
import com.ninezero.mealtoday.model.HotList
import com.ninezero.mealtoday.model.MealList
import com.ninezero.mealtoday.model.BannerList
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
    suspend fun getBannerMeals(@Query("a") categoryName: String): Response<BannerList>

    @GET("search.php")
    suspend fun getSearchMeals(@Query("s") searchQuery: String): Response<MealList>

}