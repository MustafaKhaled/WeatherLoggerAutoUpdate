package com.kot.weatherloggerautoupdate.domain.repo

import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.util.ResultCode

interface UpdateWeatherRepo {

    suspend fun getCurrentWeather() : ResultCode<WeatherResponse>?
    fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String)
}