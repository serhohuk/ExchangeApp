package com.example.exchangeapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exchangeapp.application.MyApp
import com.example.exchangeapp.repository.MainRepository
import com.example.exchangeapp.room.AppDao

class AppViewModelFactory() : ViewModelProvider.Factory {


    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        MainRepository(MyApp.database.getCurrencyDao())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(repository) as T
    }
}