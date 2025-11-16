package com.example.practica1.data

import java.io.Serializable

data class DailyForecastResponse (
    val city_name: String,
    val country_code: String,
    val data: List<DailyForecast>
): Serializable

data class DailyForecast (
    val valid_date: String,          // Fecha del pronóstico
    val max_temp: Double,            // Temperatura máxima
    val min_temp: Double,            // Temperatura mínima
    val pop: Int,                    // Probabilidad de precipitación (%)
    val precip: Double,              // Precipitación acumulada (mm)
    val rh: Int,                     // Humedad relativa (%)
    val wind_spd: Double,            // Velocidad del viento (m/s)
    val weather: DailyWeather        // Objeto con icono y descripción
): Serializable

data class DailyWeather (
    val description: String,
    val icon: String
): Serializable