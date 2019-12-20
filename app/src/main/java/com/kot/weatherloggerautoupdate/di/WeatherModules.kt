package com.kot.weatherloggerautoupdate.di

import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.data.repo.UpdateWeatherRepoImpl
import com.kot.weatherloggerautoupdate.data.repo.WeatherRepoImpl
import com.kot.weatherloggerautoupdate.domain.repo.UpdateWeatherRepo
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.presentation.viewmodel.CurrentWeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val WeatherModules = module{
    factory { createCurrentWeatherRepoInsrance(get(),get(), get())}
    factory { createUpdateWeatherRepInstance(get(),get()) }
    viewModel { CurrentWeatherViewModel(get()) }


}
fun createCurrentWeatherRepoInsrance(apiServices: ApiServices, weatherDao: WeatherDao, sharedPreferenceManager: SharedPreferenceManager): WeatherRepo {
    return WeatherRepoImpl(apiServices,weatherDao, sharedPreferenceManager)


}
fun createUpdateWeatherRepInstance(apiServices: ApiServices, sharedPreferenceManager: SharedPreferenceManager): UpdateWeatherRepo{
    return UpdateWeatherRepoImpl(apiServices,sharedPreferenceManager)
}