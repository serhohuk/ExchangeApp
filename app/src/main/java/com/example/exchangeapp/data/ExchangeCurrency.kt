package com.example.exchangeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_table")
data class ExchangeCurrency(
    val fr: String,
    @PrimaryKey
    val id: String,
    val to: String,
    val `val`: Double
)