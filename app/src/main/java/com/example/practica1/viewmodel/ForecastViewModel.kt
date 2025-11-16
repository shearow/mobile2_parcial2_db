package com.example.practica1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica1.api_service.RetrofitClient
import com.example.practica1.data.DailyForecastResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ForecastViewModel: ViewModel() {
    private val api = RetrofitClient.weatherApiService

    private val _forecast = MutableLiveData<DailyForecastResponse?>(null)
    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _error = MutableLiveData<String?>(null)

    val forecast: LiveData<DailyForecastResponse?> = _forecast
    val isLoading: LiveData<Boolean> = _isLoading
    val error: LiveData<String?> = _error

    fun getForecast(city: String, days: Int = 8) {
        val cityTrim = city.trim()
        if (cityTrim.isEmpty()) {
            _error.value = "Enter a valid city"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _forecast.value = null

            try {
                val response = api.getDailyForecast(city = cityTrim, days = days)

                if (response.data.isNotEmpty()) {
                    _forecast.value = response
                } else {
                    _error.value = "The city of \"$cityTrim\" not found"
                }
            } catch (e: HttpException) {
                _error.value = "Server error: ${e.code()}"
            } catch (e: IOException) {
                _error.value = "Connection error: check your network"
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage ?: "unknown"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() { _error.value = null }

    fun clearForecast() { _forecast.value = null }
}