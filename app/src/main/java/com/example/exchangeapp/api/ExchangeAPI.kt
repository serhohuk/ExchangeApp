package com.example.exchangeapp.api

import com.example.exchangeapp.data.CurrenciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeAPI {


    @GET("/api/v7/currencies")
    suspend fun getCurrenciesList(
        @Query("apiKey")
        apiKey : String
    ) : Response<CurrenciesResponse>

}