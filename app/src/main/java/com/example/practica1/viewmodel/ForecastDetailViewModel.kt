package com.example.practica1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica1.data.DailyForecast
import com.example.practica1.data.DailyForecastResponse
import com.example.practica1.repository.FavoriteCitiesRepositorySingleton
import kotlinx.coroutines.launch

class ForecastDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _forecastList = MutableLiveData<List<DailyForecast>>()
    private val _mainDay = MutableLiveData<DailyForecast>()
    private val _cityName = MutableLiveData<String>()
    private val _countryCode = MutableLiveData<String>()
    private val _isFavorite = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String?>()

    val forecastList: LiveData<List<DailyForecast>> = _forecastList
    val mainDay: LiveData<DailyForecast> = _mainDay
    val cityName: LiveData<String> = _cityName
    val countryCode: LiveData<String> = _countryCode
    val isFavorite: LiveData<Boolean> = _isFavorite
    val error: LiveData<String?> = _error

    private var forecastResponse: DailyForecastResponse? = null
    private var currentList: List<DailyForecast> = emptyList()

    fun loadForecast(response: DailyForecastResponse?) {
        if (response == null) {
            _error.value = "No forecast data received"
            return
        }

        forecastResponse = response
        currentList = response.data

        _cityName.value = response.city_name
        _countryCode.value = response.country_code
        _forecastList.value = currentList
        _mainDay.value = currentList.firstOrNull()

        viewModelScope.launch {
            val isFav = FavoriteCitiesRepositorySingleton.isFavorite(response.city_name)
            _isFavorite.value = isFav
        }
    }

    fun selectDay(position: Int) {
        currentList.getOrNull(position)?.let {
            _mainDay.value = it
        }
    }

    fun toggleFavorite() {
        val city = forecastResponse?.city_name ?: return
        viewModelScope.launch {
            val current = _isFavorite.value ?: false

            if (current) {
                FavoriteCitiesRepositorySingleton.deleteCityByName(city)
            } else {
                FavoriteCitiesRepositorySingleton.insertCity(city)
            }

            _isFavorite.postValue(!current)
        }
    }
}
