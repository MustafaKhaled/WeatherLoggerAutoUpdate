package com.kot.weatherloggerautoupdate

import com.kot.weatherloggerautoupdate.data.remote.ApiServices
import com.kot.weatherloggerautoupdate.domain.model.WeatherResponse
import com.kot.weatherloggerautoupdate.domain.repo.WeatherRepo
import com.kot.weatherloggerautoupdate.presentation.viewmodel.CurrentWeatherViewModel
import com.kot.weatherloggerautoupdate.util.ResultCode
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@ExperimentalCoroutinesApi
class CoroutineWeatherTest {
    private var success: ResultCode<WeatherResponse>? = null
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun finish() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun testYourFunc() = testScope.runBlockingTest {
        mock<WeatherRepo> {
            onBlocking { getCurrentWeather("121", "121", BuildConfig.API_KEY).let {
                success = it

            }}
        }
    }

}