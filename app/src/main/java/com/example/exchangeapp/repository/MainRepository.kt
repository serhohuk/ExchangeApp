package com.example.exchangeapp.repository

import com.example.exchangeapp.api.RetrofitInstance
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.data.ExchangeCurrency
import com.example.exchangeapp.room.AppDao

class MainRepository(private val dao : AppDao) {

    suspend fun getCurrenciesList() = RetrofitInstance.api.getCurrenciesList(RetrofitInstance.API_KEY)

    suspend fun getExchangeCourse(query : String) =
        RetrofitInstance.api.getExchangeCourse(query, RetrofitInstance.API_KEY)

    suspend fun insertCurrency(currency: Currency) = dao.insertCurrency(currency)

    suspend fun insertExchangeCourse(exchangeCurrency: ExchangeCurrency) = dao.insertExchangeCourse(exchangeCurrency)

    val readAllCurrency = dao.readAllCurrency()

    val readAllExchange = dao.readAllExchange()


}