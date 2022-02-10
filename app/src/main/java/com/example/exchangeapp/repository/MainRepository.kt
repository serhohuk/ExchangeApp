package com.example.exchangeapp.repository

import com.example.exchangeapp.api.RetrofitInstance

class MainRepository {

    suspend fun getCurrenciesList() = RetrofitInstance.api.getCurrenciesList(RetrofitInstance.API_KEY)
}