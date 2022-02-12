package com.example.exchangeapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.data.ExchangeCurrency

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Query("SELECT * FROM currency_table")
    fun readAllCurrency() : LiveData<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeCourse(exchangeCurrency: ExchangeCurrency)

    @Query("SELECT * FROM exchange_table")
    fun readAllExchange() : LiveData<List<ExchangeCurrency>>



}