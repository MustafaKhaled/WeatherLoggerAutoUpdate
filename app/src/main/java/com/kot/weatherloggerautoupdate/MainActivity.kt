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
import android.widget.Toast
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
    private val REQUEST_CHECK_SETTINGS: Int = 100
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationManager: LocationManager
    private var requestingLocationUpdates = false
    private lateinit var locationListener: LocationListener
    private var check = false
    lateinit var task: Task<LocationSettingsResponse>
    private val currentPosition = CurrentPosition()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isLocationEnabled = false
    var locationStateReceiver: BroadcastReceiver? = null
    private val currentWeatherViewModel: CurrentWeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeLoadingWeather()


        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.v("MainActivity() ",location.latitude.toString())
                    // Update UI with location data
                    // ...
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
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
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
        task.addOnSuccessListener(fun(locationSettingsResponse: LocationSettingsResponse) {
            getLastKnownLocation()
        })
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }


    }


    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Toast.makeText(this, location!!.longitude.toString(), Toast.LENGTH_LONG).show()
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


    private fun observeLoadingWeather() {
        currentWeatherViewModel.loadWeather("35", "139").observe(this) {
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

    }

    class CurrentPosition {
        var log = 0.0
        var lat = 0.0
    }
}
