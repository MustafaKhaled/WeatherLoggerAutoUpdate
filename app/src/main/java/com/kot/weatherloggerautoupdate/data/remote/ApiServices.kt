package com.kot.weatherloggerautoupdate.data.remote
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.util.Result
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices{

    @GET("weather") suspend fun getLatestNewsAsync(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("APPID") apiKey: String
        ): Response<WeatherResponse>

}