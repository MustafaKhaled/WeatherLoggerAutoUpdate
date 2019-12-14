package com.kot.weatherloggerautoupdate.di

import androidx.room.Room
import com.kot.weatherloggerautoupdate.data.presistance.room.WeatherDatabase
import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val PersistenceModule = module{
    single { SharedPreferenceManager(get()) }
    single {
        Room.databaseBuilder(androidContext(), WeatherDatabase::class.java,
            "app-database").build()
    }

    factory {get<WeatherDatabase>().weatherDao}
}