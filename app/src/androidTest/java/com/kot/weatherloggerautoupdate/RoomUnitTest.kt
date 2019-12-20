package com.kot.weatherloggerautoupdate

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kot.weatherloggerautoupdate.data.presistance.room.WeatherDatabase
import com.kot.weatherloggerautoupdate.data.presistance.room.dao.WeatherDao
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.Assert.assertThat
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomUnitTest {
    private lateinit var weatherDao: WeatherDao
    private lateinit var database: WeatherDatabase

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java
        ).build()
        weatherDao = database.weatherDao
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertToDatabase() = runBlocking {
        val weatherLogs = mutableListOf<WeatherEntity>()
        val log = WeatherEntity("123", "10/12/2012", "1344")
        weatherDao.insert(log)
        weatherLogs.addAll(weatherDao.getAll())

        assertThat(weatherLogs.size, equalTo(1))


    }
}