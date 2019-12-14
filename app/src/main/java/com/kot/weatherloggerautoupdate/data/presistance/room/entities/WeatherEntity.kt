package com.kot.weatherloggerautoupdate.data.presistance.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "weather_items")
data class WeatherEntity(val temperature: String, val date: String, val pressure: String ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}