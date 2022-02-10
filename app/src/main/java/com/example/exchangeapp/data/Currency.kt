package com.example.exchangeapp.data

import java.io.Serializable

data class Currency(
    val currencyName: String,
    val currencySymbol: String,
    val id: String
) : Serializable