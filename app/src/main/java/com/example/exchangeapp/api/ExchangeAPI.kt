package com.example.exchangeapp.api

import com.example.exchangeapp.data.CurrenciesResponse
import com.example.exchangeapp.data.ExchangeData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeAPI {


    @GET("/api/v7/currencies")
    suspend fun getCurrenciesList(
        @Query("apiKey")
        apiKey : String
    ) : Response<CurrenciesResponse>

    @GET("api/v7/convert")
    suspend fun getExchangeCourse(
        @Query("q")
        query : String,
        @Query("apiKey")
        apiKey: String
    ) : Response<ExchangeData>

}