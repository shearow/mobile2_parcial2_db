package com.example.practica1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica1.api_service.RetrofitClient
import com.example.practica1.data.City
import com.example.practica1.data.CurrentWeatherData
import com.example.practica1.repository.FavoriteCitiesRepositorySingleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class CurrentWeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.weatherApiService

    private val _weatherList = MutableLiveData<List<CurrentWeatherData>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String?>()

    val weatherList: LiveData<List<CurrentWeatherData>> get() = _weatherList
    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<String?> get() = _error

    // Automatically check the database
    init {
        FavoriteCitiesRepositorySingleton.allCities.observeForever { cities ->
            loadWeatherFor(cities)
        }
    }

    private fun loadWeatherFor(cities: List<City>) {
        _isLoading.value = true
        _error.value = null

        if (cities.isEmpty()) {
            _weatherList.value = emptyList()
            _error.value = "You don’t have any favorite cities yet"
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            try {
                val deferred = cities.map { city ->
                    async {
                        try {
                            apiService.getCurrentWeather(city.name).data.firstOrNull()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                val result = deferred.awaitAll().filterNotNull()

                if (result.isEmpty()) {
                    _error.value = "Weather data could not be loaded"
                }

                _weatherList.value = result

            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(cityName: String) {
        viewModelScope.launch {
            FavoriteCitiesRepositorySingleton.deleteCityByName(cityName)
        }
    }
}