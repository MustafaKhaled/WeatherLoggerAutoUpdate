package com.kot.weatherloggerautoupdate.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result
import retrofit2.Response
import retrofit2.await
import java.io.IOException
import java.lang.Exception
import kotlin.math.ln

class WeatherRepoImpl(private val apiServices: ApiServices, private val weatherDao: WeatherDao) : WeatherRepo {
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

}