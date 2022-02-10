package com.example.exchangeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.data.CurrenciesResponse
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.repository.MainRepository
import com.example.exchangeapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AppViewModel() : ViewModel() {

    private var repository : MainRepository = MainRepository()

    val currenciesListData = MutableLiveData<Resource<List<Currency>>>();

    fun getCurrenciesList(){
        viewModelScope.launch {
            currenciesListData.postValue(Resource.Loading())
            val response = repository.getCurrenciesList()
            handleCurrenciesListResponse(response)
        }
    }

    private fun handleCurrenciesListResponse(response : Response<CurrenciesResponse>){
        try {
            currenciesListData.postValue(handleCurrenciesList(response))
        }
        catch (t : Throwable){
            currenciesListData.postValue(Resource.Error("Something went wrong"))
        }

    }

    private fun handleCurrenciesList(response: Response<CurrenciesResponse>): Resource<List<Currency>> {
        if (response.isSuccessful){
            response.body()?.let { responseBody ->
                return Resource.Succes(responseBody.results.values.toList().sortedBy { it.currencyName })
            }
        }
        return Resource.Error(response.message())
    }
}