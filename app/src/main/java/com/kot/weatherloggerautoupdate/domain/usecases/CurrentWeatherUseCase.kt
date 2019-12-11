package com.kot.weatherloggerautoupdate.domain.usecases

import androidx.lifecycle.LiveData
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result

class CurrentWeatherUseCase constructor(private val weatherRepo: WeatherRepo) {
//    suspend fun getCurrentWeather(lat: String, lng: String, apiKey: String): Result<WeatherResponse>? {
//        return weatherRepo.getCurrentWeather(lat,lng,apiKey)
//    }
}