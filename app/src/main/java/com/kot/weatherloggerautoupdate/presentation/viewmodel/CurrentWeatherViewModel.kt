package com.kot.weatherloggerautoupdate.presentation.viewmodel

import androidx.lifecycle.*
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.domain.usecases.CurrentWeatherUseCase
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result
import kotlinx.coroutines.*

class CurrentWeatherViewModel(private val weatherRepo: WeatherRepo) :
    ViewModel() {
    fun loadWeather(lat: String, lng: String)  : LiveData<Result<WeatherResponse>?>{
           return  liveData(context =  Dispatchers.IO){
               emit(Result.Loading())
                emit(weatherRepo.getCurrentWeather(lat,lng,BuildConfig.API_KEY))
            }
        }



}