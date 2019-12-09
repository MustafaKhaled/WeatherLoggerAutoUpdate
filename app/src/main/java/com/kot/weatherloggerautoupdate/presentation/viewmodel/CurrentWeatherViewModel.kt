package com.kot.weatherloggerautoupdate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.usecases.CurrentWeatherUseCase
import com.kot.weatherloggerautoupdate.util.ResponseApi
import com.kot.weatherloggerautoupdate.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentWeatherViewModel(private val currentWeatherUseCase: CurrentWeatherUseCase) :
    ViewModel() {
    private val _properties = MutableLiveData<Result<WeatherResponse>>()
    private val _loadingStatus = MutableLiveData<Boolean>()

    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus
    val properties: LiveData<Result<WeatherResponse>>
        get() = _properties

    init {
        _loadingStatus.postValue(true)
    }

    fun loadWeather(lat: String, lng: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = currentWeatherUseCase.getCurrentWeather(lat, lng, BuildConfig.API_KEY)
            when (result) {
                is Result.Success<WeatherResponse> -> {
                    _properties.postValue(result.also {
                        it.data }
                    )
                    _loadingStatus.postValue(false)
                }
                is Error -> {
                    _properties.postValue(result.also { it })
                    _loadingStatus.postValue(false)
                }
            }

        }
    }


}