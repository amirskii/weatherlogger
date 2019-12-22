package com.example.weatherlogger.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherlogger.api.ApiResponse
import com.example.weatherlogger.api.ApiService
import com.example.weatherlogger.models.Resource
import com.example.weatherlogger.models.Weather
import com.example.weatherlogger.models.WeatherResponse
import com.example.weatherlogger.room.WeatherDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyRepository @Inject constructor(val service: ApiService,
                                       val locationData: LocationLiveData,
                                       val gpsUtils: GpsUtils,
                                       val weatherDao: WeatherDao) {

    val APP_ID = "f5906cea85954fdd25f4ec694c1857ca"
    val UNITS = "metric"

    init {
    }

    fun <T>getNetworkData(serviceCall: () -> LiveData<ApiResponse<T>>): LiveData<Resource<T>> {
        return object: NetworkOnlyRepository<T, T>() {
            override fun saveLoadedData(item: T) {
            }

            override fun fetchService(): LiveData<ApiResponse<T>> {
                return serviceCall()
            }

            override fun onFetchFailed(error: Throwable?) {
                Log.d("111", "onFetchFailed : $error")
            }

        }.asLiveData()
    }

    fun saveWeatherResponse(resp: WeatherResponse, date: Date) {
        GlobalScope.launch(Dispatchers.Default) {
            weatherDao.insertWeather(Weather(resp.main.temp, date))
        }
    }

    fun getWeather(lat: Double, lon: Double, date: Date): LiveData<Resource<WeatherResponse>> {
        return object: NetworkOnlyRepository<WeatherResponse, WeatherResponse>() {
            override fun saveLoadedData(item: WeatherResponse) {
                saveWeatherResponse(item, date)
            }

            override fun fetchService(): LiveData<ApiResponse<WeatherResponse>> {
                return service.getWeather(lat, lon, APP_ID, UNITS)
            }

            override fun onFetchFailed(error: Throwable?) {
                Log.d("111","onFetchFailed : $error")
            }

        }.asLiveData()
    }

    fun loadWeatherLocally() = weatherDao.selectWeather()

//    fun getWeather(lat: Double, lon: Double): LiveData<Resource<WeatherResponse>> {
//        return getNetworkData{ service.getWeather(lat, lon, APP_ID, UNITS) }
//    }
}
