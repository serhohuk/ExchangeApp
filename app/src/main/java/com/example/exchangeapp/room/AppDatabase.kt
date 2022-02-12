package com.example.exchangeapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.data.ExchangeCurrency
import java.util.*

@Database(entities = [Currency::class, ExchangeCurrency::class],version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getCurrencyDao() : AppDao
}