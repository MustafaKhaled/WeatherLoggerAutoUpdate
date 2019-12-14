package com.kot.weatherloggerautoupdate.data.presistance.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(private val context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "weather-pref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val currentTempKey: String = "currTemp"
    private val maxTempKey: String = "maxTemp"
    private val minTempKey: String = "minTemp"

    fun getLatestWeather(): WeatherPersistence{
        return WeatherPersistence(sharedPref.getString(currentTempKey,null),sharedPref.getString(maxTempKey,null),sharedPref.getString(minTempKey,null))
    }

    fun updateWeatherPresistence(currentTemp: String,
                                 maxTemp: String,
                                 minTemp: String){
        sharedPref.edit().putString(currentTempKey,currentTemp).apply()
        sharedPref.edit().putString(maxTempKey,maxTemp).apply()
        sharedPref.edit().putString(minTempKey,minTemp).apply()


    }


    data class WeatherPersistence(val currentTemp: String?, val maxTemp: String?, val minTemp: String? )
}