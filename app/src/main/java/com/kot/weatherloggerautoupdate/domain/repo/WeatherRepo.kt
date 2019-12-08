package com.kot.weatherloggerautoupdate.domain.repo
import com.kot.weatherloggerautoupdate.util.ResponseApi
import retrofit2.Response

interface WeatherRepo{
    fun getCurrentWeather();
}