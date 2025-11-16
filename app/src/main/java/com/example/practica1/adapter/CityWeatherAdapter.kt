package com.example.practica1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.data.CurrentWeatherData
import com.example.practica1.databinding.ItemCityBinding
import com.squareup.picasso.Picasso

class CityWeatherAdapter(
    private var list: List<CurrentWeatherData>,
    private val onCityClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<CityWeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvWeatherCityName.text = "${item.city_name}, ${item.country_code}"
        holder.binding.tvWeatherTemperature.text = "${item.temp}°C"
        holder.binding.tvWeatherDescription.text = "${item.weather.description}"

        val iconUrl = "https://www.weatherbit.io/static/img/icons/${item.weather.icon}.png"
        Picasso.get().load(iconUrl).into(holder.binding.imgWeatherBackground)

        holder.binding.cardCity.setOnClickListener {
            onCityClick(item.city_name)
        }

        holder.binding.btnDeleteCity.setOnClickListener {
            onDeleteClick(item.city_name)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateList(newList: List<CurrentWeatherData>) {
        list = newList
        notifyDataSetChanged()
    }
}