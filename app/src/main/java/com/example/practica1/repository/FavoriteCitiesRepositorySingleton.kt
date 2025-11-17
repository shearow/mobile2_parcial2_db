package com.example.practica1.repository

import FavoriteCityDbHelper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practica1.data.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FavoriteCitiesRepositorySingleton {
    private var dbHelper: FavoriteCityDbHelper? = null

    private val _allCities = MutableLiveData<List<City>>()
    val allCities : LiveData<List<City>> = _allCities

    fun initialize(context: Context){
        if (dbHelper == null){
            dbHelper = FavoriteCityDbHelper(context)
            loadCities()
        }
    }

    private fun requireDbHelper(): FavoriteCityDbHelper{
        return dbHelper ?: throw IllegalStateException(
            "El repository no ha sido inicializado. Llama a initialize"
        )
    }

    private fun loadCities(){
        _allCities.value = requireDbHelper().getAllCities()
    }

    suspend fun insertCity(cityName: String){
        withContext(Dispatchers.IO){
            requireDbHelper().insertCity(cityName)
            withContext(Dispatchers.Main){
                loadCities()
            }
        }
    }

    suspend fun deleteCity(id: Long){
        withContext(Dispatchers.IO){
            requireDbHelper().deleteCityById(id)
            withContext(Dispatchers.Main){
                loadCities()
            }
        }
    }

    suspend fun deleteCityByName(cityName: String) {
        withContext(Dispatchers.IO) {
            requireDbHelper().deleteCityByName(cityName)
            withContext(Dispatchers.Main) {
                loadCities()
            }
        }
    }

    suspend fun isFavorite(cityName: String): Boolean {
        return withContext(Dispatchers.IO) {
            requireDbHelper().isFavorite(cityName)
        }
    }
}