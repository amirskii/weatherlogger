package com.example.weatherlogger.ui.main

import androidx.lifecycle.ViewModel
import com.example.weatherlogger.repository.GpsUtils
import com.example.weatherlogger.repository.MyRepository
import javax.inject.Inject

class MainViewModel @Inject
constructor(private val repository: MyRepository): ViewModel() {
    var isGPSEnabled = false
    val gpsUtils = repository.gpsUtils
    fun getLocationData() = repository.locationData

    init {
        gpsUtils.turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }
        })
    }

}
