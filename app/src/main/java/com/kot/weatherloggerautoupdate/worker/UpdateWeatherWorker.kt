package com.kot.weatherloggerautoupdate.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.MainActivity
import com.kot.weatherloggerautoupdate.R
import com.kot.weatherloggerautoupdate.domain.repo.UpdateWeatherRepo
import com.kot.weatherloggerautoupdate.util.ResultCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class UpdateWeatherWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params),
    KoinComponent {
    private val updateWeatherRepo: UpdateWeatherRepo by inject()
    override suspend fun doWork(): Result = coroutineScope {
        try {
            updateWeatherRepo.getCurrentWeather()
            return@coroutineScope Result.success()

        }
        catch (e: Exception){
            return@coroutineScope Result.failure()

        }
    }

}

