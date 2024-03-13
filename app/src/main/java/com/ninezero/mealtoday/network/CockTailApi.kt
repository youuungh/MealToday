package com.ninezero.mealtoday.network

import com.ninezero.mealtoday.model.BeverageList
import com.ninezero.mealtoday.model.CocktailList
import com.ninezero.mealtoday.model.DrinkList
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