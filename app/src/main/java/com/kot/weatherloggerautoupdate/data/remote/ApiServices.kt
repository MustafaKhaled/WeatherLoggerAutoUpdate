package com.kot.weatherloggerautoupdate.data.remote
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices{

    @GET suspend fun getLatestNewsAsync(
        @Query("lat") searchKeyWord: String,
        @Query("APPID") apiKey: String,
        @Query("lng") pageNumber: String
    ): WeatherResponse

}