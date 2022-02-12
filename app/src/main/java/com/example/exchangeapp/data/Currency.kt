package com.example.exchangeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "currency_table")
data class Currency(
    val currencyName: String,
    val currencySymbol: String?,
    @PrimaryKey
    val id: String
) : Serializable