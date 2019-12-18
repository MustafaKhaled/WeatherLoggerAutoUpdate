package com.kot.weatherloggerautoupdate.presentation.viewmodel

import androidx.lifecycle.*
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.domain.usecases.CurrentWeatherUseCase
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result
import kotlinx.coroutines.*
import java.lang.Exception

class CurrentWeatherViewModel(private val weatherRepo: WeatherRepo) :
    ViewModel() {
    var insertItemLiveData = MutableLiveData<Result<Exception>>()

    fun loadWeather(lat: String, lng: String): LiveData<Result<WeatherResponse>?> {
        return liveData(context = Dispatchers.IO) {
            emit(Result.Loading())
            emit(weatherRepo.getCurrentWeather(lat, lng, BuildConfig.API_KEY))
        }
    }


    fun getAllItems(): LiveData<Result<List<WeatherEntity>>>{
        return liveData(context = Dispatchers.IO) {
            emit(Result.Loading())
            try {
               emit(Result.Success(weatherRepo.loadWeatherPersistence()))
            }
            catch (e: Exception){
                emit(Result.Error(e))
            }
        }

    }

    fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String){
        weatherRepo.updateSharedPreference(currentTemp,maxTemp, minTemp)
    }

    fun insertItem(weatherEntity: WeatherEntity){
        viewModelScope.launch(context = Dispatchers.IO){
            insertItemLiveData.postValue(Result.Loading())
            try {
                weatherRepo.insertItem(weatherEntity)
                insertItemLiveData.postValue(Result.Success(null))

            }
            catch (e: Exception){
                insertItemLiveData.postValue(Result.Error(e))
            }
        }
    }

    fun getInformationStored(): SharedPreferenceManager.WeatherPersistence{
        return weatherRepo.checkStoredData()
    }

    fun updateFirstTime(){
        weatherRepo.updateFirstTime()
    }


}