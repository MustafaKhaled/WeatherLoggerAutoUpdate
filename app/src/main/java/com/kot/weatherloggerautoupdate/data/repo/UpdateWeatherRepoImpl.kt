package com.kot.weatherloggerautoupdate.data.repo

import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.MainActivity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.UpdateWeatherRepo
import com.kot.weatherloggerautoupdate.util.ResultCode
import java.io.IOException

class UpdateWeatherRepoImpl(private val apiServices: ApiServices, private  val sharedPreferenceManager: SharedPreferenceManager): UpdateWeatherRepo {
        override suspend fun getCurrentWeather(): ResultCode<WeatherResponse>? {
        val latLng: MainActivity.LatLng = sharedPreferenceManager.getLatestLatLng()
        val response = apiServices.getWeather(latLng.lat.toString(),latLng.lng.toString(),BuildConfig.API_KEY)
        return if (response.isSuccessful) {
            updateSharedPreference(response.body()!!.main.temp.toString(),response.body()!!.main.temp_max.toString(),response.body()!!.main.temp_min.toString())
            ResultCode.Success(response.body())
        } else {
            ResultCode.Error(IOException("Error occurred during fetching movies!"))
        }
    }

    override fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String) {
        sharedPreferenceManager.updateWeatherPersistence(currentTemp,maxTemp,minTemp)
    }
}