package com.kot.weatherloggerautoupdate.presentation.viewmodel

import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.util.MyApplication
import com.kot.weatherloggerautoupdate.util.ResultCode
import com.kot.weatherloggerautoupdate.worker.UpdateWeatherWorker
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import java.lang.Exception
import java.util.concurrent.TimeUnit

class CurrentWeatherViewModel(private val weatherRepo: WeatherRepo) :
    ViewModel() , KoinComponent{
    private val workManager = WorkManager.getInstance(MyApplication.myInstance)

    var insertItemLiveData = MutableLiveData<ResultCode<Exception>>()

    fun loadWeather(lat: String, lng: String): LiveData<ResultCode<WeatherResponse>?> {
        return liveData(context = Dispatchers.IO) {
            emit(ResultCode.Loading())
            emit(weatherRepo.getCurrentWeather(lat, lng, BuildConfig.API_KEY))
        }
    }

    internal fun callWorkManager(){
            val workRequest = PeriodicWorkRequest.Builder(UpdateWeatherWorker::class.java,120000,TimeUnit.MILLISECONDS)
                .build()
            workManager.enqueue(workRequest)

    }

    fun getAllItems(): LiveData<ResultCode<List<WeatherEntity>>>{
        return liveData(context = Dispatchers.IO) {
            emit(ResultCode.Loading())
            try {
               emit(ResultCode.Success(weatherRepo.loadWeatherPersistence()))
            }
            catch (e: Exception){
                emit(ResultCode.Error(e))
            }
        }

    }

    fun updateSharedPreference(currentTemp: String, maxTemp: String, minTemp: String){
        weatherRepo.updateSharedPreference(currentTemp,maxTemp, minTemp)
    }

    fun insertItem(weatherEntity: WeatherEntity){
        viewModelScope.launch(context = Dispatchers.IO){
            insertItemLiveData.postValue(ResultCode.Loading())
            try {
                weatherRepo.insertItem(weatherEntity)
                insertItemLiveData.postValue(ResultCode.Success(null))

            }
            catch (e: Exception){
                insertItemLiveData.postValue(ResultCode.Error(e))
            }
        }
    }

    fun updateLatLng(lat: String, lng: String){
        weatherRepo.updateLatLng(lat, lng)
    }

    fun getInformationStored(): SharedPreferenceManager.WeatherPersistence{
        return weatherRepo.checkStoredData()
    }

    fun updateFirstTime(){
        weatherRepo.updateFirstTime()
    }


}