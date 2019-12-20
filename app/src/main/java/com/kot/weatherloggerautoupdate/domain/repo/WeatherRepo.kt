package com.kot.weatherloggerautoupdate.domain.repo
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.util.ResultCode

interface WeatherRepo{
    suspend fun getCurrentWeather(lat: String, lng: String, appId: String) : ResultCode<WeatherResponse>?
    suspend fun loadWeatherPersistence(): List<WeatherEntity>
    suspend fun insertItem(weatherEntity: WeatherEntity)
    fun updateLatLng(lat: String, lng: String)
    fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String)
    fun updateFirstTime()
    fun checkStoredData():SharedPreferenceManager.WeatherPersistence
}