package com.example.practica1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.data.DailyForecast
import com.example.practica1.databinding.ItemForecastDayBinding
import com.example.practica1.utils.getDayOfWeek
import com.squareup.picasso.Picasso

class ForecastAdapter(
    private var list: List<DailyForecast> = emptyList(),
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    private var selectedPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForecastDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val dayText = if (position == 0) {
            "Today"
        } else {
            getDayOfWeek(item.valid_date)
        }
        holder.binding.tvDayName.text = dayText
        holder.binding.tvTempMin.text = "${item.min_temp.toInt()}°"
        holder.binding.tvTempMax.text = "${item.max_temp.toInt()}°"

        val iconUrl = "https://www.weatherbit.io/static/img/icons/${item.weather.icon}.png"
        Picasso.get().load(iconUrl).into(holder.binding.imgWeatherIcon)

        if (position == selectedPosition) {
            holder.binding.root.setCardBackgroundColor(0xFF333333.toInt())
            holder.binding.tvDayName.setTextColor(0xFFFFFFFF.toInt())
            holder.binding.tvTempMin.setTextColor(0xFFFFFFFF.toInt())
            holder.binding.tvTempMax.setTextColor(0xFFFFFFFF.toInt())
        } else {
            holder.binding.root.setCardBackgroundColor(0xFFFFFFFF.toInt())
            holder.binding.tvDayName.setTextColor(0xFF000000.toInt())
            holder.binding.tvTempMin.setTextColor(0xFF000000.toInt())
            holder.binding.tvTempMax.setTextColor(0xFF000000.toInt())
        }

        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: ItemForecastDayBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newList: List<DailyForecast>) {
        list = newList
        selectedPosition = 0
        notifyDataSetChanged()
    }
}