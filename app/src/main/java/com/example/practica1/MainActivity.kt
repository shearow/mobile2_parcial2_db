package com.example.practica1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1.adapter.CityWeatherAdapter
import com.example.practica1.databinding.ActivityMainBinding
import com.example.practica1.repository.FavoriteCitiesRepositorySingleton
import com.example.practica1.viewmodel.CurrentWeatherViewModel
import com.example.practica1.viewmodel.ForecastViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vmCurrentWeather: CurrentWeatherViewModel
    private lateinit var vmForecastViewModel: ForecastViewModel
    private lateinit var adapter: CityWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Database
        FavoriteCitiesRepositorySingleton.initialize(applicationContext)

        vmCurrentWeather = ViewModelProvider(this)[CurrentWeatherViewModel::class.java]
        vmForecastViewModel = ViewModelProvider(this)[ForecastViewModel::class.java]

        adapter = CityWeatherAdapter(
            emptyList(),
            onCityClick = { cityName ->
                vmForecastViewModel.getForecast(cityName)
            },
            onDeleteClick = { cityName ->
                vmCurrentWeather.removeFavorite(cityName)
            }
        )
        binding.rvCities.adapter = adapter
        binding.rvCities.layoutManager = LinearLayoutManager(this)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnSearchCity.setOnClickListener {
            val city = binding.etSearchCity.text.toString().trim()
            if (city.isNotEmpty()) {
                vmForecastViewModel.getForecast(city)
            } else {
                Toast.makeText(this, "Enter a city", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {

        vmCurrentWeather.weatherList.observe(this) { list ->
            adapter.updateList(list)

        }

        vmCurrentWeather.error.observe(this) { errorMessage ->
            if(errorMessage != null) {
                binding.tvErrorFavoriteCities.text = errorMessage
                binding.tvErrorFavoriteCities.visibility = View.VISIBLE
                binding.rvCities.visibility = View.GONE
            }else {
                binding.tvErrorFavoriteCities.visibility = View.GONE
                binding.rvCities.visibility = View.VISIBLE
            }
        }

        vmCurrentWeather.isLoading.observe(this) { isLoading ->
            if(isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        vmForecastViewModel.forecast.observe(this) { forecast ->
            forecast?.let {
                val intent = Intent(this, ForecastDetailActivity::class.java)
                intent.putExtra("forecastData", it)
                startActivity(intent)
                vmForecastViewModel.clearForecast()
            }
        }

        vmForecastViewModel.isLoading.observe(this) { isLoading ->
            if(isLoading) {
                binding.searchCityProgressBar.visibility = View.VISIBLE
                binding.btnSearchCity.isEnabled = false
                binding.etSearchCity.isEnabled = false
            } else {
                binding.searchCityProgressBar.visibility = View.GONE
                binding.btnSearchCity.isEnabled = true
                binding.etSearchCity.isEnabled = true
            }
        }

        vmForecastViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.etSearchCity.text?.clear()
                vmForecastViewModel.clearError()
            }
        }
    }
}