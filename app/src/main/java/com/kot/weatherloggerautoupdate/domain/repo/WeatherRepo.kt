package com.kot.weatherloggerautoupdate.domain.repo
import androidx.lifecycle.LiveData
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result
import retrofit2.Response

interface WeatherRepo{
    suspend fun getCurrentWeather(lat: String, lng: String, appId: String) : Result<WeatherResponse>?
    suspend fun loadWeatherPersistence(): List<WeatherEntity>
    suspend fun insertItem(weatherEntity: WeatherEntity)
    fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String)
    fun updateFirstTime()
    fun checkStoredData():SharedPreferenceManager.WeatherPersistence
}