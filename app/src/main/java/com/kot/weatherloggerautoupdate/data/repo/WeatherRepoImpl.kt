package com.kot.weatherloggerautoupdate.data.repo

import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.util.Result
import java.io.IOException

class WeatherRepoImpl(private val apiServices: ApiServices, private val weatherDao: WeatherDao, private val sharedPreferenceManager: SharedPreferenceManager) : WeatherRepo {
    override suspend fun getCurrentWeather(
        lat: String,
        lng: String,
        appId: String
    ): Result<WeatherResponse> {
        val response = apiServices.getLatestNewsAsync(lat, lng, appId)
        return if (response.isSuccessful) {
            Result.Success(response.body())
        } else {
            Result.Error(IOException("Error occurred during fetching movies!"))
        }
    }

    override suspend fun loadWeatherPersistence(): List<WeatherEntity> {
        return weatherDao.getAll()
    }

    override suspend fun insertItem(weatherEntity: WeatherEntity) {
        weatherDao.insert(weatherEntity)
    }

    override fun updateSharedPreference(currentTemp: String,
                                        maxTemp: String,
                                        minTemp: String) {
        sharedPreferenceManager.updateWeatherPersistence(currentTemp,maxTemp,minTemp)
    }

    override fun updateFirstTime() {
        sharedPreferenceManager.updateFirstTime()
    }

    override fun checkStoredData(): SharedPreferenceManager.WeatherPersistence {
        return sharedPreferenceManager.getLatestWeather()
    }

}