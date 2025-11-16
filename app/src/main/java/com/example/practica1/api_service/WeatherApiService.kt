package com.example.practica1.api_service

import com.example.practica1.Config
import com.example.practica1.data.CurrentWeatherResponse
import com.example.practica1.data.DailyForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("city") city: String,
        @Query("key") apiKey: String = Config.WEATHERBIT_API_KEY
    ): CurrentWeatherResponse

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("city") city: String,
        @Query("days") days: Int = 7,
        @Query("key") apiKey: String = Config.WEATHERBIT_API_KEY,
    ): DailyForecastResponse
}