package com.kot.weatherloggerautoupdate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kot.weatherloggerautoupdate.R
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import kotlinx.android.synthetic.main.weather_logger_list_item.view.*

class PersistenceWeatherAdapter:
    RecyclerView.Adapter<PersistenceWeatherAdapter.WeatherListItemViewHolder>() {
    var mainList: ArrayList<WeatherEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListItemViewHolder {
        return WeatherListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_logger_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    override fun onBindViewHolder(holder: WeatherListItemViewHolder, position: Int) {
        holder.bind(mainList[position])
    }

    fun addItem(weatherEntity: WeatherEntity){
        mainList.add(weatherEntity)
        notifyItemInserted(mainList.size-1)
    }

    fun addAll(weatherList: List<WeatherEntity>){
        this.mainList.addAll(weatherList)
        notifyItemRangeInserted(mainList.size,weatherList.size)
    }


    class WeatherListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherEntity: WeatherEntity){
            itemView.date_value.text = weatherEntity.date
            itemView.pressure_value.text = weatherEntity.pressure
            itemView.temp_value.text = weatherEntity.temperature
        }

    }
}