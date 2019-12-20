package com.kot.weatherloggerautoupdate.data.presistance.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity

@Dao
abstract class WeatherDao {
    @Query("SELECT * FROM WeatherEntity")
    abstract fun getAll(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(weatherItem: WeatherEntity)
}