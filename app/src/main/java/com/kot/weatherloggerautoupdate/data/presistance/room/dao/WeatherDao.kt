package com.kot.weatherloggerautoupdate.data.presistance.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_items")
    fun getAll(): LiveData<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherItem: WeatherEntity)
}