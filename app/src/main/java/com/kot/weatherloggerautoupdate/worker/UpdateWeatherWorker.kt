package com.kot.weatherloggerautoupdate.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.MainActivity
import com.kot.weatherloggerautoupdate.domain.repo.UpdateWeatherRepo
import com.kot.weatherloggerautoupdate.util.ResultCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class UpdateWeatherWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params),
    KoinComponent {
    private val updateWeatherRepo: UpdateWeatherRepo by inject()
    override suspend fun doWork(): Result = coroutineScope {
        val call = async {
            updateWeatherRepo.getCurrentWeather()
        }
        call.await()
        Result.success()
    }
}

