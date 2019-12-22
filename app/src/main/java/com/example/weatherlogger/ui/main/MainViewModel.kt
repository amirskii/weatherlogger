package com.example.weatherlogger.ui.main

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weatherlogger.models.Status
import com.example.weatherlogger.repository.GpsUtils
import com.example.weatherlogger.repository.MyRepository
import com.example.weatherlogger.utils.combineAndCompute
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject
constructor(private val repository: MyRepository): ViewModel() {
    var isGPSEnabled = false
    val gpsUtils = repository.gpsUtils

    private val initialTime = SystemClock.elapsedRealtime()
    private val timer = Timer()
    private val INITIAL_DELAY = 500L
    private val INTERVAL = 3000L
    val elapsedTimeData = MutableLiveData<Long>()

    init {
        gpsUtils.turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }
        })

        // Update the elapsed time every second.
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val newValue = (SystemClock.elapsedRealtime() - initialTime) / 1000
                elapsedTimeData.postValue(newValue)
            }
        }, INITIAL_DELAY, INTERVAL)
    }

    fun getLocationData() = repository.locationData

    // ask for location every INTERVAL
    val locationTick = elapsedTimeData.combineAndCompute(getLocationData()) { _, location ->
        location
    }

    // ask for weather every time we get location
    val weatherAt = Transformations.switchMap(locationTick) { location ->
        repository.getWeather(location.latitude, location.longitude, Date())
    }

    fun saveWeather() {
        weatherAt.value?.let { resource ->
            if (resource.status == Status.SUCCESS) {
                resource.data?.let {
                    repository.saveWeatherResponse(it, Date())
                }
            }
        }
    }

    fun getWeatherLocally() = repository.loadWeatherLocally()

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}
