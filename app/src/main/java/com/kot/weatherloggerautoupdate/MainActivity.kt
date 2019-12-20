package com.kot.weatherloggerautoupdate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.kot.weatherloggerautoupdate.data.presistance.room.entities.WeatherEntity
import com.kot.weatherloggerautoupdate.data.presistance.sharedpref.SharedPreferenceManager
import com.kot.weatherloggerautoupdate.presentation.viewmodel.CurrentWeatherViewModel
import com.kot.weatherloggerautoupdate.ui.adapter.PersistenceWeatherAdapter
import com.kot.weatherloggerautoupdate.util.ResultCode
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.*

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val TAG = "MainActivity"
    private val REQUEST_CHECK_SETTINGS: Int = 100
    private lateinit var informationStored: SharedPreferenceManager.WeatherPersistence
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationManager: LocationManager
    private var requestingLocationUpdates = false
    private var latLng: LatLng = LatLng()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val weatherAdapter: PersistenceWeatherAdapter = PersistenceWeatherAdapter()
    private val currentWeatherViewModel: CurrentWeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        latLng.lat = location.latitude.toInt()
                        latLng.lng = location.longitude.toInt()
                        currentWeatherViewModel.updateLatLng(
                            latLng.lat.toString(),
                            latLng.lng.toString()
                        )
                        getWeather((latLng.lat).toString(), (latLng.lng).toString())
                    }
                }
            }
        }
        setUpRecyclerView()
        observeDataInsertion()
        getPersistenceWeatherLog()

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissionsRequired()
        } else {
            getCurrentLocation()
            populateHighlightedWeather()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_btn) {
            getWeather(latLng.lat.toString(), latLng.lng.toString())
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weather_menu, menu)
        return true
    }

    private fun populateHighlightedWeather() {
        informationStored = currentWeatherViewModel.getInformationStored()
        current_temp.text = informationStored.currentTemp
        max_temp.text = informationStored.maxTemp
        min_temp.text = informationStored.minTemp
        last_update_date.text = String.format("%s %s", getString(R.string.last_update_label),informationStored.date)
    }

    private fun setUpRecyclerView() {
        recyclerView.adapter = weatherAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getPersistenceWeatherLog() {
        currentWeatherViewModel.getAllItems().observe(this) {
            when (it) {
                is ResultCode.Loading -> {
                }
                is ResultCode.Success -> {
                    if (it.data?.size == 0 || it.data != null) {
                        val weatherList = it.data
                        weatherAdapter.addAll(weatherList)
                    }
                }
                is ResultCode.Error -> {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeDataInsertion() {
        currentWeatherViewModel.insertItemLiveData.observe(this) {
            when (it) {
                is ResultCode.Loading -> {
                }
                is ResultCode.Success -> {
                    Snackbar.make(recyclerView, "Weather log saved", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates)
            startLocationUpdates()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationRequest(): LocationRequest {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        return locationRequest
    }

    fun getCurrentLocation() {

        val builder = createLocationRequest()?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
        }
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder?.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            if (locationSettingsResponse.locationSettingsStates.isLocationPresent) {
                getLastKnownLocation()
            }
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {

                    exception.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }


    }
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latLng.lat = location.latitude.toInt()
                latLng.lng = location.longitude.toInt()
                currentWeatherViewModel.updateLatLng(latLng.lat.toString(), latLng.lng.toString())
                getWeather(latLng.lat.toString(),latLng.lng.toString())
            } else {
                startLocationUpdates()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == RESULT_OK) {
                    /*Take your data*/
                    requestingLocationUpdates = true
                } else {
                    finish()
                }
            }
        }
    }


    private fun getWeather(lat: String, lng: String) {
        currentWeatherViewModel.loadWeather(lat, lng)
            .observe(this) {
                when (it) {
                    is ResultCode.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is ResultCode.Success -> {
                        progressBar.visibility = View.GONE
                        val temperature = it.data!!.main.temp.toString()
                        val date = Date().toString()
                        val pressure = it.data.main.pressure.toString()
                        val weatherItem = WeatherEntity(
                            temperature, date,
                            pressure
                        )
                        currentWeatherViewModel.insertItem(weatherItem)
                        weatherAdapter.addItem(weatherItem)
                        if (currentWeatherViewModel.getInformationStored().isFirstTime) {
                            currentWeatherViewModel.callWorkManager()
                            currentWeatherViewModel.updateFirstTime()
                            currentWeatherViewModel.updateLatLng(
                                latLng.lat.toString(),
                                latLng.lng.toString()
                            )
                            currentWeatherViewModel.updateSharedPreference(temperature,it.data.main.temp_max.toString(),it.data.main.temp_min.toString())
                            populateHighlightedWeatherFromNetwork(
                                temperature,
                                it.data.main.temp_max.toString(),
                                it.data.main.temp_min.toString()
                            )
                        }
                    }
                    is ResultCode.Error -> {
                        progressBar.visibility = View.GONE
                    }

                }

            }
    }
    private fun populateHighlightedWeatherFromNetwork(
        currentTemp: String,
        maxTemp: String,
        minTemp: String
    ) {
        current_temp.text = currentTemp
        max_temp.text = maxTemp
        min_temp.text = minTemp
        last_update_date.text =
            String.format("%s %s", getString(R.string.last_update_label), Date().toString())
    }
    private fun requestPermissionsRequired() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, 100, Manifest.permission.ACCESS_COARSE_LOCATION).build()
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getCurrentLocation()
    }
    class LatLng() {
        var lng = 0
        var lat = 0

        constructor(newLat: Int, newLng: Int) : this() {
            lng = newLng
            lat = newLat
        }
    }
}
