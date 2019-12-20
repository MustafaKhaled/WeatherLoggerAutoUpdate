package com.kot.weatherloggerautoupdate.util

import android.app.Application
import com.kot.weatherloggerautoupdate.di.NetworkModule
import com.kot.weatherloggerautoupdate.di.PersistenceModule
import com.kot.weatherloggerautoupdate.di.WeatherModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(NetworkModule, WeatherModules, PersistenceModule))
        }

    }

    companion object {
        private lateinit var instance: Application

        val myInstance: Application
            get() {
                if (instance == null) {
                    instance = Application()
                }

                return instance
            }
    }
}