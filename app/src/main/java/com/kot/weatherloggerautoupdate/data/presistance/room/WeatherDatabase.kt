package com.kot.weatherloggerautoupdate.data.presistance.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1,
    exportSchema = false)
abstract class WeatherDatabase: RoomDatabase(){
    abstract val weatherDao: WeatherDao
}