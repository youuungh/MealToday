package com.example.mealtoday.network

import com.example.mealtoday.model.BeverageList
import com.example.mealtoday.model.CocktailList
import com.example.mealtoday.model.DrinkList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CockTailApi {

    @GET("filter.php")
    suspend fun getCocktails(@Query("c") cocktailName: String): Response<CocktailList>

    @GET("search.php")
    suspend fun getDrinks(@Query("f") drinkName: String): Response<DrinkList>

    @GET("lookup.php")
    suspend fun getBeverageInfo(@Query("i") beverageId: String): Response<BeverageList>

}