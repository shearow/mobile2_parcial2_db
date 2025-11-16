package com.example.practica1.repository

import android.content.Context

class FavoriteCitiesRepository(context: Context) {

    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val key = "favorite_cities"

    fun getFavorites(): List<String> {
        val set = prefs.getStringSet(key, emptySet()) ?: emptySet()
        return set.toList()
    }

    fun addFavorite(city: String) {
        val currentSet = prefs.getStringSet(key, emptySet())?.toMutableSet() ?: mutableSetOf()
        currentSet.add(city)
        prefs.edit().putStringSet(key, currentSet).apply()
    }

    fun removeFavorite(city: String) {
        val currentSet = prefs.getStringSet(key, emptySet())?.toMutableSet() ?: mutableSetOf()
        currentSet.remove(city)
        prefs.edit().putStringSet(key, currentSet).apply()
    }

    fun isFavorite(city: String): Boolean {
        return getFavorites().contains(city)
    }
}