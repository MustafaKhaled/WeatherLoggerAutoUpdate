package com.kot.weatherloggerautoupdate

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.kot.weatherloggerautoupdate.presentation.viewmodel.CurrentWeatherViewModel
import com.kot.weatherloggerautoupdate.util.Result
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val TAG ="MainActivity"
    private val REQUEST_CHECK_SETTINGS: Int = 100
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationManager: LocationManager
    private var requestingLocationUpdates = false
    private var latLng: LatLng = LatLng()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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
                        latLng.lat = location.latitude
                        latLng.lng = location.longitude


                        getWeather((latLng.lat.toInt()).toString(),(latLng.lng.toInt()).toString())
                    }
                }
            }
        }

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissionsRequired()
        } else {
            getCurrentLocation()
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
            if(locationSettingsResponse.locationSettingsStates.isLocationPresent){
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


    fun getLastKnownLocation(){
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latLng.lat = location.latitude
                latLng.lng = location.longitude
                currentWeatherViewModel.loadWeather(latLng.lat.toString(),latLng.lng.toString())
                Log.v(TAG,"Longitude: LastKnownLocation "+latLng.lat.toString())
                getWeather((latLng.lat.toInt()).toString(),(latLng.lng.toInt()).toString())


            }
            else{
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
                }
            }
        }
    }


    private fun getWeather(lat: String, lng:String) {
        currentWeatherViewModel.loadWeather(lat,lng)
            .observe(this) {
                when (it) {
                    is Result.Loading -> {
                        progressParent.visibility = View.VISIBLE
                        current_temp_layout.visibility = View.GONE
                    }
                    is Result.Success -> {
                        progressParent.visibility = View.GONE
                        current_temp_layout.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                        current_temp_layout.visibility = View.VISIBLE
                    }
                }

            }
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

    class LatLng {
        var lng = 0.0
        var lat = 0.0
    }
}
