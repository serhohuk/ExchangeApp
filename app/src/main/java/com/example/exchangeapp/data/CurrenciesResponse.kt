package com.example.exchangeapp.data

import java.io.Serializable

data class CurrenciesResponse(
    var results : Map<String, Currency>
) : Serializable
