package com.example.exchangeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.data.CurrenciesResponse
import com.example.exchangeapp.data.Currency
import com.example.exchangeapp.data.ExchangeCurrency
import com.example.exchangeapp.data.ExchangeData
import com.example.exchangeapp.repository.MainRepository
import com.example.exchangeapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AppViewModel(private var repository: MainRepository) : ViewModel() {

    val readAllCurrency : LiveData<List<Currency>>
    val readAllExchangeCurrency : LiveData<List<ExchangeCurrency>>

    init {
        readAllCurrency = repository.readAllCurrency
        readAllExchangeCurrency = repository.readAllExchange
    }


    val currenciesListData = MutableLiveData<Resource<List<Currency>>>();
    val exchangeData = MutableLiveData<Resource<ExchangeCurrency>>();

    fun setCurrenciesListData(){
//        currenciesListData.postValue(Resource.Succes(readAllCurrency.))
    }

    fun getExchangeCourse(query : String){
        viewModelScope.launch {
            val response = repository.getExchangeCourse(query)
            if (response.isSuccessful){
                response.body()?.let {
                    exchangeData.postValue(Resource.Succes(it.results.values.toList()[0]))
                }
            } else{
                exchangeData.postValue(Resource.Error(message = response.message()))
            }
        }
    }

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

    fun insertCurrency(currency: Currency){
        viewModelScope.launch {
            repository.insertCurrency(currency)
        }
    }

    fun insertExchangeCourse(exchangeCurrency: ExchangeCurrency){
        viewModelScope.launch {
            repository.insertExchangeCourse(exchangeCurrency)
        }
    }


}