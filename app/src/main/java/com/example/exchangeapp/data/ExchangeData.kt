package com.example.exchangeapp.data

data class ExchangeData(
    val query: Query,
    val results: Map<String,ExchangeCurrency>
)