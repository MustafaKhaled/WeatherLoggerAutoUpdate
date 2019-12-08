package com.kot.weatherloggerautoupdate.data.repo
import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.util.ResponseApi
import retrofit2.Response

class WeatherRepoImpl(private val apiServices : ApiServices) : WeatherRepo {
    override  fun getCurrentWeather(){

    }

}