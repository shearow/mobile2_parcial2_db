package com.example.practica1.data

data class CurrentWeatherResponse (
    val data: List<CurrentWeatherData>
)

data class CurrentWeatherData (
    val city_name: String,
    val country_code: String,
    val temp: Double,
    val weather: WeatherInfo
)

data class WeatherInfo (
    val description: String,
    val icon: String
)
