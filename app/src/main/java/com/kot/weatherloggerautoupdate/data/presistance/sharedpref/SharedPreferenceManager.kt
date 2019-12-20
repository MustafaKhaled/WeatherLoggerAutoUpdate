package com.kot.weatherloggerautoupdate.data.presistance.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.kot.weatherloggerautoupdate.MainActivity
import java.util.*

class SharedPreferenceManager(context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "weather-pref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val currentTempKey: String = "currTemp"
    private val maxTempKey: String = "maxTemp"
    private val minTempKey: String = "minTemp"
    private val firstTimeKey: String = "firstTime"
    private val latKey: String = "lat"
    private val lngKey: String = "lng"
    private val lastUpdate: String = "lastUpdate"


    fun getLatestWeather(): WeatherPersistence{
        return WeatherPersistence(sharedPref.getString(currentTempKey,null),
            sharedPref.getString(maxTempKey,null),
            sharedPref.getString(minTempKey,null),
            sharedPref.getString(lastUpdate,null),
            sharedPref.getBoolean(firstTimeKey,true)
            )
    }

    fun updateWeatherPersistence(currentTemp: String,
                                 maxTemp: String,
                                 minTemp: String){
        sharedPref.edit().putString(currentTempKey,currentTemp).apply()
        sharedPref.edit().putString(maxTempKey,maxTemp).apply()
        sharedPref.edit().putString(minTempKey,minTemp).apply()
        sharedPref.edit().putString(lastUpdate,Date().toString()).apply()

    }

    fun updateFirstTime(){
        sharedPref.edit().putBoolean(firstTimeKey, false).apply()
    }

    fun updateLatLng(lat: String, lng: String){
        sharedPref.edit().putString(latKey,lat).apply()
        sharedPref.edit().putString(lngKey,lng).apply()
    }

    fun getLatestLatLng(): MainActivity.LatLng{
        val lat = sharedPref.getString(latKey,"0")
        val lng = sharedPref.getString(lngKey,"0")
        return MainActivity.LatLng(lat!!.toInt(),lng!!.toInt())
    }




    data class WeatherPersistence(val currentTemp: String?, val maxTemp: String?, val minTemp: String?,val date: String?, val isFirstTime: Boolean )
}