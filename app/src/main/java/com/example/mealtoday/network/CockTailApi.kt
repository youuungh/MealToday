package com.example.mealtoday.network

import com.example.mealtoday.data.DrinkList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CockTailApi {

    @GET("filter.php")
    suspend fun getDrinks(@Query("c") drinkName: String): Response<DrinkList>

}