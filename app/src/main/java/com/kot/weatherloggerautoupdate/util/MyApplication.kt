package com.kot.weatherloggerautoupdate.util

import android.app.Application
import com.kot.weatherloggerautoupdate.di.NetworkModule
import com.kot.weatherloggerautoupdate.di.WeatherModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(NetworkModule, WeatherModules))
        }

    }
}